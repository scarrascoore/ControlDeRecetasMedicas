package com.portafolio.controlrecetamedica.application.prescription.usecase;

import com.portafolio.controlrecetamedica.application.prescription.dto.PrescriptionResponse;
import com.portafolio.controlrecetamedica.domain.prescription.repository.PrescriptionRepositoryPort;
import com.portafolio.controlrecetamedica.domain.specialty.repository.SpecialtyRepositoryPort;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class GetPrescriptionUseCase {

    private final PrescriptionRepositoryPort repo;
    private final UserRepositoryPort userRepo;
    private final SpecialtyRepositoryPort specialtyRepo;

    public GetPrescriptionUseCase(
            PrescriptionRepositoryPort repo,
            UserRepositoryPort userRepo,
            SpecialtyRepositoryPort specialtyRepo
    ) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.specialtyRepo = specialtyRepo;
    }

    public PrescriptionResponse execute(String currentUserEmail, boolean isAdmin, Long id) {
        var prescription = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Receta no encontrada"));

        if (!isAdmin) {
            var user = userRepo.findByEmail(currentUserEmail.toLowerCase())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            if (!prescription.getUserId().equals(user.getId())) {
                throw new IllegalArgumentException("No tienes acceso a esta receta");
            }
        }

        var specialtyName = specialtyRepo.findById(prescription.getSpecialtyId())
                .map(s -> s.getName())
                .orElse("Desconocida");

        return new PrescriptionResponse(
                prescription.getId(),
                prescription.getSpecialtyId(),
                specialtyName,
                prescription.getTitle(),
                prescription.getNotes(),
                prescription.getCreatedAt()
        );
    }
}

