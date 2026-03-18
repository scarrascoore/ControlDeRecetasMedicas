package com.portafolio.controlrecetamedica.application.prescription.usecase;

import com.portafolio.controlrecetamedica.application.prescription.dto.CreatePrescriptionRequest;
import com.portafolio.controlrecetamedica.application.prescription.dto.PrescriptionResponse;
import com.portafolio.controlrecetamedica.domain.prescription.model.Prescription;
import com.portafolio.controlrecetamedica.domain.prescription.repository.PrescriptionRepositoryPort;
import com.portafolio.controlrecetamedica.domain.specialty.repository.SpecialtyRepositoryPort;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CreatePrescriptionUseCase {

    private final PrescriptionRepositoryPort repo;
    private final UserRepositoryPort userRepo;
    private final SpecialtyRepositoryPort specialtyRepo;

    public CreatePrescriptionUseCase(
            PrescriptionRepositoryPort repo,
            UserRepositoryPort userRepo,
            SpecialtyRepositoryPort specialtyRepo
    ) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.specialtyRepo = specialtyRepo;
    }

    public PrescriptionResponse execute(String currentUserEmail, CreatePrescriptionRequest req) {
        var user = userRepo.findByEmail(currentUserEmail.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        var specialty = specialtyRepo.findById(req.getSpecialtyId())
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));

        if (!specialty.isActive()) {
            throw new IllegalArgumentException("Especialidad inactiva");
        }

        var created = repo.save(new Prescription(
                null,
                user.getId(),
                req.getSpecialtyId(),
                req.getTitle().trim(),
                req.getNotes() == null ? null : req.getNotes().trim(),
                Instant.now()
        ));

        return new PrescriptionResponse(
                created.getId(),
                created.getSpecialtyId(),
                specialty.getName(),
                created.getTitle(),
                created.getNotes(),
                created.getCreatedAt()
        );
    }
}

