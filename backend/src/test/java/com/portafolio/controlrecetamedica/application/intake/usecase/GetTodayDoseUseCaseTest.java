package com.portafolio.controlrecetamedica.application.intake.usecase;

import com.portafolio.controlrecetamedica.application.intake.dto.TodayDoseResponse;
import com.portafolio.controlrecetamedica.domain.intake.model.IntakeLog;
import com.portafolio.controlrecetamedica.domain.intake.model.IntakeStatus;
import com.portafolio.controlrecetamedica.domain.intake.repository.IntakeLogRepositoryPort;
import com.portafolio.controlrecetamedica.domain.prescription.model.Prescription;
import com.portafolio.controlrecetamedica.domain.prescription.repository.PrescriptionRepositoryPort;
import com.portafolio.controlrecetamedica.domain.schedule.model.FrequencyType;
import com.portafolio.controlrecetamedica.domain.schedule.model.MedicationSchedule;
import com.portafolio.controlrecetamedica.domain.schedule.repository.MedicationScheduleRepositoryPort;
import com.portafolio.controlrecetamedica.domain.user.model.Role;
import com.portafolio.controlrecetamedica.domain.user.model.User;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetTodayDosesUseCaseTest {

    private MedicationScheduleRepositoryPort scheduleRepo;
    private PrescriptionRepositoryPort prescriptionRepo;
    private UserRepositoryPort userRepo;
    private IntakeLogRepositoryPort intakeRepo;

    private GetTodayDosesUseCase useCase;

    private final ZoneId zone = ZoneId.of("America/Lima");

    @BeforeEach
    void setUp() {
        scheduleRepo = mock(MedicationScheduleRepositoryPort.class);
        prescriptionRepo = mock(PrescriptionRepositoryPort.class);
        userRepo = mock(UserRepositoryPort.class);
        intakeRepo = mock(IntakeLogRepositoryPort.class);

        useCase = new GetTodayDosesUseCase(scheduleRepo, prescriptionRepo, userRepo, intakeRepo);
    }

    @Test
    void shouldReturnTodaySlots_withStatusPendingTakenSkipped() {
        String email = "test@mail.com";
        Long userId = 10L;
        Long prescriptionId = 20L;
        Long scheduleId = 30L;

        when(userRepo.findByEmail(email.toLowerCase()))
                .thenReturn(Optional.of(new User(userId, email, "hash", Role.PACIENTE, true)));

        when(prescriptionRepo.findByUserId(userId))
                .thenReturn(List.of(new Prescription(
                        prescriptionId, userId, 1L, "Receta 1", null, Instant.now()
                )));

        LocalDate today = LocalDate.now(zone);

        MedicationSchedule schedule = new MedicationSchedule(
                scheduleId, prescriptionId,
                "Amoxicilina", "500mg",
                FrequencyType.DAILY, 3, null,
                today.minusDays(1), null,
                true, LocalTime.of(8, 0)
        );

        when(scheduleRepo.findByPrescriptionId(prescriptionId))
                .thenReturn(List.of(schedule));

        Instant from = today.atStartOfDay(zone).toInstant();
        Instant to = today.plusDays(1).atStartOfDay(zone).toInstant();

        Instant slot0800 = today.atTime(8, 0).atZone(zone).toInstant();
        Instant slot1600 = today.atTime(16, 0).atZone(zone).toInstant();

        when(intakeRepo.findByScheduleIdBetween(scheduleId, from, to))
                .thenReturn(List.of(
                        new IntakeLog(1L, scheduleId, slot0800, IntakeStatus.TAKEN, Instant.now(), "ok"),
                        new IntakeLog(2L, scheduleId, slot1600, IntakeStatus.SKIPPED, Instant.now(), "no pude")
                ));

        List<TodayDoseResponse> res = useCase.execute(email);

        // 3 slots: 08:00, 16:00, 00:00 (por regla 24/3=8h => 08, 16, 00 del día siguiente pero en LocalTime queda 00:00)
        // Si no te gusta el 00:00, luego ajustamos regla para que no cruce medianoche.
        assertEquals(3, res.size());

        TodayDoseResponse at0800 = res.stream()
                .filter(r -> r.getPlannedTime().equals(LocalTime.of(8,0)))
                .findFirst().orElseThrow();

        TodayDoseResponse at1600 = res.stream()
                .filter(r -> r.getPlannedTime().equals(LocalTime.of(16,0)))
                .findFirst().orElseThrow();

        TodayDoseResponse at0000 = res.stream()
                .filter(r -> r.getPlannedTime().equals(LocalTime.of(0,0)))
                .findFirst().orElseThrow();

        assertEquals("TAKEN", at0800.getStatus());
        assertEquals("SKIPPED", at1600.getStatus());
        assertEquals("PENDING", at0000.getStatus());

        // extra: taken boolean coherente con status
        assertTrue(at0800.isTaken());
        assertFalse(at1600.isTaken());
        assertFalse(at0000.isTaken());
    }

    @Test
    void shouldReturnEmpty_whenNoSchedules() {
        String email = "test@mail.com";
        Long userId = 10L;

        when(userRepo.findByEmail(email.toLowerCase()))
                .thenReturn(Optional.of(new User(userId, email, "hash", Role.PACIENTE, true)));

        when(prescriptionRepo.findByUserId(userId)).thenReturn(List.of());

        List<TodayDoseResponse> res = useCase.execute(email);
        assertTrue(res.isEmpty());
    }
}
