package com.portafolio.controlrecetamedica.application.specialty.usecase;

import com.portafolio.controlrecetamedica.application.specialty.dto.SpecialtyResponse;
import com.portafolio.controlrecetamedica.application.specialty.dto.SpecialtyUpsertRequest;
import com.portafolio.controlrecetamedica.domain.specialty.model.Specialty;
import com.portafolio.controlrecetamedica.domain.specialty.repository.SpecialtyRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class UpdateSpecialtyUseCase {

    private final SpecialtyRepositoryPort repo;

    public UpdateSpecialtyUseCase(SpecialtyRepositoryPort repo) {
        this.repo = repo;
    }

    public SpecialtyResponse execute(Long id, SpecialtyUpsertRequest req) {
        Specialty current = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));

        String newName = req.getName().trim();
        boolean newActive = (req.getActive() == null) ? current.isActive() : req.getActive();

        // Si cambió el nombre, valida duplicado
        if (!current.getName().equalsIgnoreCase(newName) && repo.existsByNameIgnoreCase(newName)) {
            throw new IllegalArgumentException("La especialidad ya existe");
        }

        Specialty updated = repo.save(new Specialty(current.getId(), newName, newActive));

        return new SpecialtyResponse(updated.getId(), updated.getName(), updated.isActive());
    }
}
