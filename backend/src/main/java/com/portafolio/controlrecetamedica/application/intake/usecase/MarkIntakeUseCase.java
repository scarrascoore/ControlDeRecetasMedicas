package com.portafolio.controlrecetamedica.application.intake.usecase;

import com.portafolio.controlrecetamedica.application.intake.dto.IntakeLogResponse;
import com.portafolio.controlrecetamedica.application.intake.dto.MarkIntakeRequest;
import com.portafolio.controlrecetamedica.domain.intake.model.IntakeLog;
import com.portafolio.controlrecetamedica.domain.intake.model.IntakeStatus;
import com.portafolio.controlrecetamedica.domain.intake.repository.IntakeLogRepositoryPort;
import com.portafolio.controlrecetamedica.domain.prescription.repository.PrescriptionRepositoryPort;
import com.portafolio.controlrecetamedica.domain.schedule.model.MedicationSchedule;
import com.portafolio.controlrecetamedica.domain.schedule.repository.MedicationScheduleRepositoryPort;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class MarkIntakeUseCase {

    private final IntakeLogRepositoryPort intakeRepo;
    private final MedicationScheduleRepositoryPort scheduleRepo;
    private final PrescriptionRepositoryPort prescriptionRepo;
    private final UserRepositoryPort userRepo;

    private final ZoneId zone = ZoneId.of("America/Lima");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public MarkIntakeUseCase(
            IntakeLogRepositoryPort intakeRepo,
            MedicationScheduleRepositoryPort scheduleRepo,
            PrescriptionRepositoryPort prescriptionRepo,
            UserRepositoryPort userRepo
    ) {
        this.intakeRepo = intakeRepo;
        this.scheduleRepo = scheduleRepo;
        this.prescriptionRepo = prescriptionRepo;
        this.userRepo = userRepo;
    }

    public IntakeLogResponse execute(String email, boolean isAdmin, Long scheduleId, MarkIntakeRequest req) {
        if (scheduleId == null) throw new IllegalArgumentException("scheduleId es obligatorio");
        if (req == null) throw new IllegalArgumentException("request es obligatorio");
        if (req.getPlannedTime() == null || req.getPlannedTime().isBlank())
            throw new IllegalArgumentException("plannedTime es obligatorio (HH:mm)");
        if (req.getStatus() == null || req.getStatus().isBlank())
            throw new IllegalArgumentException("status es obligatorio (TAKEN|SKIPPED)");

        var schedule = scheduleRepo.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule no encontrado"));

        var prescription = prescriptionRepo.findById(schedule.getPrescriptionId())
                .orElseThrow(() -> new IllegalArgumentException("Receta no encontrada"));

        if (!isAdmin) {
            var user = userRepo.findByEmail(email.toLowerCase())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            if (!prescription.getUserId().equals(user.getId())) {
                throw new IllegalArgumentException("No tienes acceso a este schedule");
            }
        }

        // plannedAt = hoy + plannedTime (HH:mm)
        LocalTime plannedTime = parsePlannedTime(req.getPlannedTime());

        // ✅ VALIDAR que la hora es un slot válido del schedule
        validatePlannedTimeIsValidSlot(schedule, plannedTime);

        Instant plannedAt = LocalDate.now(zone).atTime(plannedTime).atZone(zone).toInstant();

        IntakeStatus status = parseStatus(req.getStatus());

        var toSave = new IntakeLog(
                null,
                scheduleId,
                plannedAt,
                status,
                Instant.now(),
                req.getNote() == null ? null : req.getNote().trim()
        );

        var saved = intakeRepo.save(toSave);

        // ✅ Defensive programming (evita NPE con mocks o fallos reales)
        if (saved == null) {
            throw new IllegalStateException("No se pudo guardar el intake log (save devolvió null)");
        }

        return new IntakeLogResponse(
                saved.getId(),
                saved.getScheduleId(),
                saved.getPlannedAt(),
                saved.getStatus().name(),
                saved.getLoggedAt(),
                saved.getNote()
        );
    }

    private static LocalTime parsePlannedTime(String raw) {
        try {
            return LocalTime.parse(raw.trim(), TIME_FMT);
        } catch (Exception ex) {
            throw new IllegalArgumentException("plannedTime inválido. Formato esperado HH:mm (ej: 08:00)");
        }
    }

    private static IntakeStatus parseStatus(String raw) {
        try {
            return IntakeStatus.valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (Exception ex) {
            throw new IllegalArgumentException("status inválido. Use TAKEN o SKIPPED");
        }
    }

    private void validatePlannedTimeIsValidSlot(MedicationSchedule schedule, LocalTime plannedTime) {
        List<LocalTime> slots = buildSlots(schedule);
        boolean ok = slots.stream().anyMatch(t -> t.equals(plannedTime));
        if (!ok) {
            throw new IllegalArgumentException("plannedTime no coincide con un slot válido. Slots: " + slots);
        }
    }

    /**
     * Regla simple para slots del día:
     * - startTime + i*interval
     * - interval = intervalHours si existe, sino 24/timesPerDay
     */
    private List<LocalTime> buildSlots(MedicationSchedule schedule) {
        if (!schedule.isActive()) throw new IllegalArgumentException("Schedule inactivo");
        if (schedule.getStartTime() == null) throw new IllegalArgumentException("startTime es obligatorio");
        if (schedule.getTimesPerDay() == null || schedule.getTimesPerDay() <= 0)
            throw new IllegalArgumentException("timesPerDay inválido");

        int timesPerDay = schedule.getTimesPerDay();
        int interval = (schedule.getIntervalHours() != null && schedule.getIntervalHours() > 0)
                ? schedule.getIntervalHours()
                : Math.max(1, 24 / timesPerDay);

        List<LocalTime> slots = new ArrayList<>(timesPerDay);
        LocalTime base = schedule.getStartTime();

        for (int i = 0; i < timesPerDay; i++) {
            slots.add(base.plusHours((long) i * interval));
        }
        return slots;
    }
}
