package com.portafolio.controlrecetamedica.application.specialty.usecase;

import com.portafolio.controlrecetamedica.domain.specialty.repository.SpecialtyRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class DeleteSpecialtyUseCase {

    private final SpecialtyRepositoryPort repo;

    public DeleteSpecialtyUseCase(SpecialtyRepositoryPort repo) {
        this.repo = repo;
    }

    public void execute(Long id) {

        repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));
        repo.deleteById(id);
    }
}
