package com.portafolio.controlrecetamedica.infrastructure.persistence.schedule.jpa;

import com.portafolio.controlrecetamedica.infrastructure.persistence.schedule.entity.MedicationScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationScheduleJpaRepository extends JpaRepository<MedicationScheduleEntity, Long> {
    List<MedicationScheduleEntity> findByPrescriptionIdOrderByIdDesc(Long prescriptionId);
}