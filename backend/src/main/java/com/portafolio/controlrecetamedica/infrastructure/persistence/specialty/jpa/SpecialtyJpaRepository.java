package com.portafolio.controlrecetamedica.infrastructure.persistence.specialty.jpa;

import com.portafolio.controlrecetamedica.infrastructure.persistence.specialty.entity.SpecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialtyJpaRepository extends JpaRepository<SpecialtyEntity, Long> {
    boolean existsByNameIgnoreCase(String name);
}
