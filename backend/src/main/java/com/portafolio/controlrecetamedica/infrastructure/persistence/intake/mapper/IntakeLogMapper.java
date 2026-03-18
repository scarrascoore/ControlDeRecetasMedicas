package com.portafolio.controlrecetamedica.infrastructure.persistence.intake.mapper;

import com.portafolio.controlrecetamedica.domain.intake.model.IntakeLog;
import com.portafolio.controlrecetamedica.infrastructure.persistence.intake.entity.IntakeLogEntity;
import com.portafolio.controlrecetamedica.domain.intake.model.IntakeStatus;

public class IntakeLogMapper {

    public static IntakeLog toDomain(IntakeLogEntity e) {
        return new IntakeLog(
                e.getId(),
                e.getScheduleId(),
                e.getPlannedAt(),
                IntakeStatus.valueOf(e.getStatus()),
                e.getLoggedAt(),
                e.getNote()
        );
    }

    public static IntakeLogEntity toEntity(IntakeLog d) {
        IntakeLogEntity e = new IntakeLogEntity();
        e.setId(d.getId());
        e.setScheduleId(d.getScheduleId());
        e.setPlannedAt(d.getPlannedAt());
        e.setStatus(d.getStatus().name());
        e.setLoggedAt(d.getLoggedAt());
        e.setNote(d.getNote());
        return e;
    }
}
