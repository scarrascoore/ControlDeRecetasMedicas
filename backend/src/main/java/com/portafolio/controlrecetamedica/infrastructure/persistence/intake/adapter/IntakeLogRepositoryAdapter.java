package com.portafolio.controlrecetamedica.infrastructure.persistence.intake.adapter;

import com.portafolio.controlrecetamedica.domain.intake.model.IntakeLog;
import com.portafolio.controlrecetamedica.domain.intake.repository.IntakeLogRepositoryPort;
import com.portafolio.controlrecetamedica.infrastructure.persistence.intake.jpa.IntakeLogJpaRepository;
import com.portafolio.controlrecetamedica.infrastructure.persistence.intake.mapper.IntakeLogMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class IntakeLogRepositoryAdapter implements IntakeLogRepositoryPort {

    private final IntakeLogJpaRepository jpa;

    public IntakeLogRepositoryAdapter(IntakeLogJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public IntakeLog save(IntakeLog log) {
        var saved = jpa.save(IntakeLogMapper.toEntity(log));
        return IntakeLogMapper.toDomain(saved);
    }

    @Override
    public List<IntakeLog> findByScheduleIdBetween(Long scheduleId, Instant from, Instant to) {
        return jpa.findByScheduleIdAndPlannedAtBetweenOrderByPlannedAtAsc(scheduleId, from, to)
                .stream()
                .map(IntakeLogMapper::toDomain)
                .toList();
    }
}