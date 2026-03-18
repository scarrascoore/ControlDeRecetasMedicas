package com.portafolio.controlrecetamedica.domain.intake.repository;

import com.portafolio.controlrecetamedica.domain.intake.model.IntakeLog;

import java.time.Instant;
import java.util.List;

public interface IntakeLogRepositoryPort {
    IntakeLog save(IntakeLog log);
    List<IntakeLog> findByScheduleIdBetween(Long scheduleId, Instant from, Instant to);
}
