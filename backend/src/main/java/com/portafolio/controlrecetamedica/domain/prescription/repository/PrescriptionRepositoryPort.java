package com.portafolio.controlrecetamedica.domain.prescription.repository;

import com.portafolio.controlrecetamedica.domain.prescription.model.Prescription;

import java.util.List;
import java.util.Optional;

public interface PrescriptionRepositoryPort {
    Prescription save(Prescription prescription);
    Optional<Prescription> findById(Long id);
    List<Prescription> findByUserId(Long userId);
    void deleteById(Long id);
}
