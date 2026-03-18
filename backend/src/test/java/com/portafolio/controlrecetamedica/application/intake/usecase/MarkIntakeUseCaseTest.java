package com.portafolio.controlrecetamedica.application.intake.usecase;

import com.portafolio.controlrecetamedica.application.intake.dto.MarkIntakeRequest;
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
import org.mockito.ArgumentCaptor;

import java.time.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MarkIntakeUseCaseTest {

    private IntakeLogRepositoryPort intakeRepo;
    private MedicationScheduleRepositoryPort scheduleRepo;
    private PrescriptionRepositoryPort prescriptionRepo;
    private UserRepositoryPort userRepo;

    private MarkIntakeUseCase useCase;

    private final ZoneId zone = ZoneId.of("America/Lima");

    @BeforeEach
    void setUp() {
        intakeRepo = mock(IntakeLogRepositoryPort.class);
        scheduleRepo = mock(MedicationScheduleRepositoryPort.class);
        prescriptionRepo = mock(PrescriptionRepositoryPort.class);
        userRepo = mock(UserRepositoryPort.class);

        useCase = new MarkIntakeUseCase(intakeRepo, scheduleRepo, prescriptionRepo, userRepo);
    }

    @Test
    void shouldSaveTaken_whenOwner_andValidSlot() {
        String email = "test@mail.com";
        boolean isAdmin = false;

        Long userId = 10L;
        Long prescriptionId = 20L;
        Long scheduleId = 30L;

        when(userRepo.findByEmail(email.toLowerCase()))
                .thenReturn(Optional.of(new User(userId, email, "hash", Role.PACIENTE, true)));

        when(prescriptionRepo.findById(prescriptionId))
                .thenReturn(Optional.of(new Prescription(
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
        when(scheduleRepo.findById(scheduleId)).thenReturn(Optional.of(schedule));

        MarkIntakeRequest req = new MarkIntakeRequest();
        req.setPlannedTime("08:00");
        req.setStatus("TAKEN");
        req.setNote("ok");

        when(intakeRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ArgumentCaptor<IntakeLog> captor = ArgumentCaptor.forClass(IntakeLog.class);

        assertDoesNotThrow(() -> useCase.execute(email, isAdmin, scheduleId, req));

        verify(intakeRepo).save(captor.capture());
        IntakeLog saved = captor.getValue();

        assertEquals(scheduleId, saved.getScheduleId());
        assertEquals(IntakeStatus.TAKEN, saved.getStatus());
        assertEquals("ok", saved.getNote());
        assertNotNull(saved.getLoggedAt());
        assertNotNull(saved.getPlannedAt());

        Instant expectedPlannedAt = today.atTime(8, 0).atZone(zone).toInstant();
        assertEquals(expectedPlannedAt, saved.getPlannedAt());
    }

    @Test
    void shouldReject_whenPlannedTimeNotInSlots() {
        String email = "admin@mail.com";
        boolean isAdmin = true;

        Long userId = 999L;
        Long prescriptionId = 20L;
        Long scheduleId = 30L;

        when(userRepo.findByEmail(email.toLowerCase()))
                .thenReturn(Optional.of(new User(userId, email, "hash", Role.ADMIN, true)));

        when(prescriptionRepo.findById(prescriptionId))
                .thenReturn(Optional.of(new Prescription(
                        prescriptionId, userId, 1L, "Receta", null, Instant.now()
                )));

        LocalDate today = LocalDate.now(zone);
        MedicationSchedule schedule = new MedicationSchedule(
                scheduleId, prescriptionId,
                "Amoxicilina", "500mg",
                FrequencyType.DAILY, 3, null,
                today.minusDays(1), null,
                true, LocalTime.of(8, 0)
        );
        when(scheduleRepo.findById(scheduleId)).thenReturn(Optional.of(schedule));

        // 🔥 Si el use case intenta guardar, este test debe fallar fuerte
        when(intakeRepo.save(any())).thenThrow(new AssertionError("save() no debe llamarse si plannedTime es inválido"));

        MarkIntakeRequest req = new MarkIntakeRequest();
        req.setPlannedTime("09:30");
        req.setStatus("TAKEN");

        assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(email, isAdmin, scheduleId, req));

        verify(intakeRepo, never()).save(any());
    }
}