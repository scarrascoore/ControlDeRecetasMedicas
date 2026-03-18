package com.portafolio.controlrecetamedica.infrastructure.persistence.schedule.adapter;

import com.portafolio.controlrecetamedica.domain.schedule.model.MedicationSchedule;
import com.portafolio.controlrecetamedica.domain.schedule.repository.MedicationScheduleRepositoryPort;
import com.portafolio.controlrecetamedica.infrastructure.persistence.schedule.jpa.MedicationScheduleJpaRepository;
import com.portafolio.controlrecetamedica.infrastructure.persistence.schedule.mapper.MedicationScheduleMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MedicationScheduleRepositoryAdapter implements MedicationScheduleRepositoryPort {

    private final MedicationScheduleJpaRepository jpa;

    public MedicationScheduleRepositoryAdapter(MedicationScheduleJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public MedicationSchedule save(MedicationSchedule schedule) {
        var saved = jpa.save(MedicationScheduleMapper.toEntity(schedule));
        return MedicationScheduleMapper.toDomain(saved);
    }

    @Override
    public Optional<MedicationSchedule> findById(Long id) {
        return jpa.findById(id).map(MedicationScheduleMapper::toDomain);
    }

    @Override
    public List<MedicationSchedule> findByPrescriptionId(Long prescriptionId) {
        return jpa.findByPrescriptionIdOrderByIdDesc(prescriptionId)
                .stream().map(MedicationScheduleMapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }
}