package com.portafolio.controlrecetamedica.application.prescription.usecase;

import com.portafolio.controlrecetamedica.domain.prescription.repository.PrescriptionRepositoryPort;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class DeletePrescriptionUseCase {

    private final PrescriptionRepositoryPort repo;
    private final UserRepositoryPort userRepo;

    public DeletePrescriptionUseCase(PrescriptionRepositoryPort repo, UserRepositoryPort userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public void execute(String currentUserEmail, boolean isAdmin, Long id) {
        var prescription = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Receta no encontrada"));

        if (!isAdmin) {
            var user = userRepo.findByEmail(currentUserEmail.toLowerCase())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            if (!prescription.getUserId().equals(user.getId())) {
                throw new IllegalArgumentException("No tienes permiso para eliminar esta receta");
            }
        }

        repo.deleteById(id);
    }
}