package com.portafolio.controlrecetamedica.application.prescription.usecase;

import com.portafolio.controlrecetamedica.application.prescription.dto.PrescriptionResponse;
import com.portafolio.controlrecetamedica.domain.prescription.repository.PrescriptionRepositoryPort;
import com.portafolio.controlrecetamedica.domain.specialty.repository.SpecialtyRepositoryPort;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListMyPrescriptionsUseCase {

    private final PrescriptionRepositoryPort repo;
    private final UserRepositoryPort userRepo;
    private final SpecialtyRepositoryPort specialtyRepo;

    public ListMyPrescriptionsUseCase(
            PrescriptionRepositoryPort repo,
            UserRepositoryPort userRepo,
            SpecialtyRepositoryPort specialtyRepo
    ) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.specialtyRepo = specialtyRepo;
    }

    public List<PrescriptionResponse> execute(String currentUserEmail) {
        var user = userRepo.findByEmail(currentUserEmail.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return repo.findByUserId(user.getId()).stream().map(p -> {
            var specialtyName = specialtyRepo.findById(p.getSpecialtyId())
                    .map(s -> s.getName())
                    .orElse("Desconocida");

            return new PrescriptionResponse(
                    p.getId(),
                    p.getSpecialtyId(),
                    specialtyName,
                    p.getTitle(),
                    p.getNotes(),
                    p.getCreatedAt()
            );
        }).toList();
    }
}
