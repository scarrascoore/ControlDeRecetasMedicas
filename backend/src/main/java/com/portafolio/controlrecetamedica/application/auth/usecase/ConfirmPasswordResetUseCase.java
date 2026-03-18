package com.portafolio.controlrecetamedica.application.auth.usecase;

import com.portafolio.controlrecetamedica.application.auth.dto.MessageResponse;
import com.portafolio.controlrecetamedica.application.auth.dto.PasswordResetConfirmRequest;
import com.portafolio.controlrecetamedica.application.auth.service.InMemoryPasswordResetTokenStore;
import com.portafolio.controlrecetamedica.domain.auth.port.AuthMailPort;
import com.portafolio.controlrecetamedica.domain.auth.port.PasswordHasherPort;
import com.portafolio.controlrecetamedica.domain.user.model.User;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class ConfirmPasswordResetUseCase {

    private final UserRepositoryPort userRepo;
    private final PasswordHasherPort hasher;
    private final InMemoryPasswordResetTokenStore tokenStore;
    private final AuthMailPort authMailPort;

    public ConfirmPasswordResetUseCase(
            UserRepositoryPort userRepo,
            PasswordHasherPort hasher,
            InMemoryPasswordResetTokenStore tokenStore,
            AuthMailPort authMailPort
    ) {
        this.userRepo = userRepo;
        this.hasher = hasher;
        this.tokenStore = tokenStore;
        this.authMailPort = authMailPort;
    }

    public MessageResponse execute(PasswordResetConfirmRequest req) {
        String email = req.getEmail().trim().toLowerCase();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!tokenStore.isValid(email, req.getResetToken())) {
            throw new IllegalArgumentException("Token de reseteo inválido o expirado");
        }

        String newHash = hasher.hash(req.getNewPassword());

        User updated = new User(
                user.getId(),
                user.getEmail(),
                newHash,
                user.getRole(),
                user.isVerified()
        );

        userRepo.save(updated);

        tokenStore.consume(req.getResetToken());
        authMailPort.sendPasswordChangedEmail(user.getEmail());

        return new MessageResponse("Contraseña actualizada correctamente. Ya puedes iniciar sesión.");
    }
}
