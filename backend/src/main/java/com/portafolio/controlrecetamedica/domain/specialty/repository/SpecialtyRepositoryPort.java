package com.portafolio.controlrecetamedica.domain.specialty.repository;

import com.portafolio.controlrecetamedica.domain.specialty.model.Specialty;

import java.util.List;
import java.util.Optional;

public interface SpecialtyRepositoryPort {
    List<Specialty> findAll();
    Optional<Specialty> findById(Long id);
    boolean existsByNameIgnoreCase(String name);
    Specialty save(Specialty specialty);
    void deleteById(Long id);
}
