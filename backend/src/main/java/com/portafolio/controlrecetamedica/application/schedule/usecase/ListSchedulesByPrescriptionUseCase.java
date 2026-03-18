package com.portafolio.controlrecetamedica.application.schedule.usecase;

import com.portafolio.controlrecetamedica.application.schedule.dto.MedicationScheduleResponse;
import com.portafolio.controlrecetamedica.domain.prescription.repository.PrescriptionRepositoryPort;
import com.portafolio.controlrecetamedica.domain.schedule.repository.MedicationScheduleRepositoryPort;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListSchedulesByPrescriptionUseCase {

    private final MedicationScheduleRepositoryPort scheduleRepo;
    private final PrescriptionRepositoryPort prescriptionRepo;
    private final UserRepositoryPort userRepo;

    public ListSchedulesByPrescriptionUseCase(
            MedicationScheduleRepositoryPort scheduleRepo,
            PrescriptionRepositoryPort prescriptionRepo,
            UserRepositoryPort userRepo
    ) {
        this.scheduleRepo = scheduleRepo;
        this.prescriptionRepo = prescriptionRepo;
        this.userRepo = userRepo;
    }

    public List<MedicationScheduleResponse> execute(String currentEmail, boolean isAdmin, Long prescriptionId) {

        var prescription = prescriptionRepo.findById(prescriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Receta no encontrada"));

        if (!isAdmin) {
            var user = userRepo.findByEmail(currentEmail.toLowerCase())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            if (!prescription.getUserId().equals(user.getId())) {
                throw new IllegalArgumentException("No tienes acceso a esta receta");
            }
        }

        return scheduleRepo.findByPrescriptionId(prescriptionId).stream()
                .map(s -> new MedicationScheduleResponse(
                        s.getId(), s.getPrescriptionId(), s.getMedicineName(), s.getDose(),
                        s.getFrequencyType().name(), s.getTimesPerDay(), s.getIntervalHours(),
                        s.getStartDate(), s.getEndDate(), s.isActive()
                ))
                .toList();
    }
}