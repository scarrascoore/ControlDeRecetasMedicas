package com.portafolio.controlrecetamedica.application.specialty.usecase;

import com.portafolio.controlrecetamedica.application.specialty.dto.SpecialtyResponse;
import com.portafolio.controlrecetamedica.application.specialty.dto.SpecialtyUpsertRequest;
import com.portafolio.controlrecetamedica.domain.specialty.model.Specialty;
import com.portafolio.controlrecetamedica.domain.specialty.repository.SpecialtyRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class CreateSpecialtyUseCase {

    private final SpecialtyRepositoryPort repo;

    public CreateSpecialtyUseCase(SpecialtyRepositoryPort repo) {
        this.repo = repo;
    }

    public SpecialtyResponse execute(SpecialtyUpsertRequest req) {
        String name = req.getName().trim();

        if (repo.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("La especialidad ya existe");
        }

        boolean active = (req.getActive() == null) ? true : req.getActive();
        Specialty created = repo.save(new Specialty(null, name, active));

        return new SpecialtyResponse(created.getId(), created.getName(), created.isActive());
    }
}
