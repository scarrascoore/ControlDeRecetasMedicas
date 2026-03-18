package com.portafolio.controlrecetamedica.application.specialty.usecase;

import com.portafolio.controlrecetamedica.application.specialty.dto.SpecialtyResponse;
import com.portafolio.controlrecetamedica.domain.specialty.repository.SpecialtyRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListSpecialtiesUseCase {

    private final SpecialtyRepositoryPort repo;

    public ListSpecialtiesUseCase(SpecialtyRepositoryPort repo) {
        this.repo = repo;
    }

    public List<SpecialtyResponse> execute() {
        return repo.findAll().stream()
                .map(s -> new SpecialtyResponse(s.getId(), s.getName(), s.isActive()))
                .toList();
    }
}