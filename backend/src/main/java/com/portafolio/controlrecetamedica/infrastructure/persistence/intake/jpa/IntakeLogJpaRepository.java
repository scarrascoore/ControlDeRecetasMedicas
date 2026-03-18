package com.portafolio.controlrecetamedica.infrastructure.persistence.intake.jpa;

import com.portafolio.controlrecetamedica.infrastructure.persistence.intake.entity.IntakeLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface IntakeLogJpaRepository extends JpaRepository<IntakeLogEntity, Long> {

    List<IntakeLogEntity> findByScheduleIdAndPlannedAtBetweenOrderByPlannedAtAsc(
            Long scheduleId, Instant from, Instant to
    );
}

