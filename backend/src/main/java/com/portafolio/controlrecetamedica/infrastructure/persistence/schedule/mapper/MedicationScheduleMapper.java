package com.portafolio.controlrecetamedica.infrastructure.persistence.schedule.mapper;

import com.portafolio.controlrecetamedica.domain.schedule.model.FrequencyType;
import com.portafolio.controlrecetamedica.domain.schedule.model.MedicationSchedule;
import com.portafolio.controlrecetamedica.infrastructure.persistence.schedule.entity.MedicationScheduleEntity;

public class MedicationScheduleMapper {

    public static MedicationSchedule toDomain(MedicationScheduleEntity e) {
        return new MedicationSchedule(
                e.getId(),
                e.getPrescriptionId(),
                e.getMedicineName(),
                e.getDose(),
                FrequencyType.valueOf(e.getFrequencyType()),
                e.getTimesPerDay(),
                e.getIntervalHours(),
                e.getStartDate(),
                e.getEndDate(),
                e.isActive(),
                e.getStartTime()
        );
    }

    public static MedicationScheduleEntity toEntity(MedicationSchedule d) {
        MedicationScheduleEntity e = new MedicationScheduleEntity();
        e.setId(d.getId());
        e.setPrescriptionId(d.getPrescriptionId());
        e.setMedicineName(d.getMedicineName());
        e.setDose(d.getDose());
        e.setFrequencyType(d.getFrequencyType().name());
        e.setTimesPerDay(d.getTimesPerDay());
        e.setIntervalHours(d.getIntervalHours());
        e.setStartDate(d.getStartDate());
        e.setEndDate(d.getEndDate());
        e.setActive(d.isActive());
        e.setStartTime(d.getStartTime());
        return e;
    }
}