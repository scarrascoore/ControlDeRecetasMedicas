package com.portafolio.controlrecetamedica.application.intake.usecase;

import com.portafolio.controlrecetamedica.application.intake.dto.IntakeLogResponse;
import com.portafolio.controlrecetamedica.domain.intake.repository.IntakeLogRepositoryPort;
import com.portafolio.controlrecetamedica.domain.prescription.repository.PrescriptionRepositoryPort;
import com.portafolio.controlrecetamedica.domain.schedule.repository.MedicationScheduleRepositoryPort;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
public class ListIntakesByScheduleUseCase {

    private final IntakeLogRepositoryPort intakeRepo;
    private final MedicationScheduleRepositoryPort scheduleRepo;
    private final PrescriptionRepositoryPort prescriptionRepo;
    private final UserRepositoryPort userRepo;

    private final ZoneId zone = ZoneId.of("America/Lima");

    public ListIntakesByScheduleUseCase(
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

    public List<IntakeLogResponse> execute(String email, boolean isAdmin, Long scheduleId, LocalDate date) {

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

        Instant from = date.atStartOfDay(zone).toInstant();
        Instant to = date.plusDays(1).atStartOfDay(zone).toInstant();

        return intakeRepo.findByScheduleIdBetween(scheduleId, from, to).stream()
                .map(l -> new IntakeLogResponse(
                        l.getId(),
                        l.getScheduleId(),
                        l.getPlannedAt(),
                        l.getStatus().name(),
                        l.getLoggedAt(),
                        l.getNote()
                ))
                .toList();

    }
}
