package com.portafolio.controlrecetamedica.infrastructure.persistence.specialty.adapter;

import com.portafolio.controlrecetamedica.domain.specialty.model.Specialty;
import com.portafolio.controlrecetamedica.domain.specialty.repository.SpecialtyRepositoryPort;
import com.portafolio.controlrecetamedica.infrastructure.persistence.specialty.jpa.SpecialtyJpaRepository;
import com.portafolio.controlrecetamedica.infrastructure.persistence.specialty.mapper.SpecialtyMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SpecialtyRepositoryAdapter implements SpecialtyRepositoryPort {

    private final SpecialtyJpaRepository jpa;

    public SpecialtyRepositoryAdapter(SpecialtyJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Specialty> findAll() {
        return jpa.findAll().stream().map(SpecialtyMapper::toDomain).toList();
    }

    @Override
    public Optional<Specialty> findById(Long id) {
        return jpa.findById(id).map(SpecialtyMapper::toDomain);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return jpa.existsByNameIgnoreCase(name);
    }

    @Override
    public Specialty save(Specialty specialty) {
        var saved = jpa.save(SpecialtyMapper.toEntity(specialty));
        return SpecialtyMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }
}
