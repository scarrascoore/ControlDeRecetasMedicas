package com.portafolio.controlrecetamedica.domain.schedule.repository;

import com.portafolio.controlrecetamedica.domain.schedule.model.MedicationSchedule;

import java.util.List;
import java.util.Optional;

public interface MedicationScheduleRepositoryPort {
    MedicationSchedule save(MedicationSchedule schedule);
    Optional<MedicationSchedule> findById(Long id);
    List<MedicationSchedule> findByPrescriptionId(Long prescriptionId);
    void deleteById(Long id);
}