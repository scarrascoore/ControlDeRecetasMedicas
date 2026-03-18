package com.portafolio.controlrecetamedica.application.schedule.usecase;

import com.portafolio.controlrecetamedica.application.schedule.dto.CreateMedicationScheduleRequest;
import com.portafolio.controlrecetamedica.application.schedule.dto.MedicationScheduleResponse;
import com.portafolio.controlrecetamedica.domain.prescription.repository.PrescriptionRepositoryPort;
import com.portafolio.controlrecetamedica.domain.schedule.model.FrequencyType;
import com.portafolio.controlrecetamedica.domain.schedule.model.MedicationSchedule;
import com.portafolio.controlrecetamedica.domain.schedule.repository.MedicationScheduleRepositoryPort;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class CreateMedicationScheduleUseCase {

    private final MedicationScheduleRepositoryPort scheduleRepo;
    private final PrescriptionRepositoryPort prescriptionRepo;
    private final UserRepositoryPort userRepo;

    public CreateMedicationScheduleUseCase(
            MedicationScheduleRepositoryPort scheduleRepo,
            PrescriptionRepositoryPort prescriptionRepo,
            UserRepositoryPort userRepo
    ) {
        this.scheduleRepo = scheduleRepo;
        this.prescriptionRepo = prescriptionRepo;
        this.userRepo = userRepo;
    }

    public MedicationScheduleResponse execute(
            String currentEmail,
            boolean isAdmin,
            Long prescriptionId,
            CreateMedicationScheduleRequest req
    ) {

        var prescription = prescriptionRepo.findById(prescriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Receta no encontrada"));

        if (!isAdmin) {
            var user = userRepo.findByEmail(currentEmail.toLowerCase())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            if (!prescription.getUserId().equals(user.getId())) {
                throw new IllegalArgumentException("No tienes acceso a esta receta");
            }
        }

        String rawFrequencyType = req.getFrequencyType() == null ? "" : req.getFrequencyType().trim().toUpperCase();

        if (!"DAILY".equals(rawFrequencyType)) {
            throw new IllegalArgumentException("Por ahora solo se permite frequencyType = DAILY");
        }

        FrequencyType type = FrequencyType.DAILY;

        if (req.getTimesPerDay() == null || req.getTimesPerDay() <= 0) {
            throw new IllegalArgumentException("timesPerDay es requerido y debe ser mayor a 0 para DAILY");
        }

        LocalDate start = (req.getStartDate() == null || req.getStartDate().isBlank())
                ? LocalDate.now()
                : LocalDate.parse(req.getStartDate());

        LocalDate end = (req.getEndDate() == null || req.getEndDate().isBlank())
                ? null
                : LocalDate.parse(req.getEndDate());

        if (end != null && end.isBefore(start)) {
            throw new IllegalArgumentException("endDate no puede ser anterior a startDate");
        }

        LocalTime startTime;
        try {
            startTime = (req.getStartTime() == null || req.getStartTime().isBlank())
                    ? LocalTime.of(8, 0)
                    : LocalTime.parse(req.getStartTime());
        } catch (Exception e) {
            throw new IllegalArgumentException("startTime inválido. Usa formato HH:mm, ejemplo 08:00");
        }

        boolean active = (req.getActive() == null) ? true : req.getActive();

        var created = scheduleRepo.save(new MedicationSchedule(
                null,
                prescriptionId,
                req.getMedicineName().trim(),
                req.getDose() == null ? null : req.getDose().trim(),
                type,
                req.getTimesPerDay(),
                null, // intervalHours se desactiva por ahora
                start,
                end,
                active,
                startTime
        ));

        return new MedicationScheduleResponse(
                created.getId(),
                created.getPrescriptionId(),
                created.getMedicineName(),
                created.getDose(),
                created.getFrequencyType().name(),
                created.getTimesPerDay(),
                created.getIntervalHours(),
                created.getStartDate(),
                created.getEndDate(),
                created.isActive()
        );
    }
}
