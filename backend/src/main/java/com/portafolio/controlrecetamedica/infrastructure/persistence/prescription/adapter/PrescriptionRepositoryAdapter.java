package com.portafolio.controlrecetamedica.infrastructure.persistence.prescription.adapter;

import com.portafolio.controlrecetamedica.domain.prescription.model.Prescription;
import com.portafolio.controlrecetamedica.domain.prescription.repository.PrescriptionRepositoryPort;
import com.portafolio.controlrecetamedica.infrastructure.persistence.prescription.jpa.PrescriptionJpaRepository;
import com.portafolio.controlrecetamedica.infrastructure.persistence.prescription.mapper.PrescriptionMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PrescriptionRepositoryAdapter implements PrescriptionRepositoryPort {

    private final PrescriptionJpaRepository jpa;

    public PrescriptionRepositoryAdapter(PrescriptionJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Prescription save(Prescription prescription) {
        var saved = jpa.save(PrescriptionMapper.toEntity(prescription));
        return PrescriptionMapper.toDomain(saved);
    }

    @Override
    public Optional<Prescription> findById(Long id) {
        return jpa.findById(id).map(PrescriptionMapper::toDomain);
    }

    @Override
    public List<Prescription> findByUserId(Long userId) {
        return jpa.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(PrescriptionMapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }
}