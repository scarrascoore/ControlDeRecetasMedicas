package com.portafolio.controlrecetamedica.infrastructure.persistence.prescription.jpa;

import com.portafolio.controlrecetamedica.infrastructure.persistence.prescription.entity.PrescriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionJpaRepository extends JpaRepository<PrescriptionEntity, Long> {
    List<PrescriptionEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
}
