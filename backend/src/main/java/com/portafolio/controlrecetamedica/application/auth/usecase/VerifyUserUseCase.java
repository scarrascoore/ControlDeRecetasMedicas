package com.portafolio.controlrecetamedica.application.auth.usecase;

import com.portafolio.controlrecetamedica.application.auth.dto.MessageResponse;
import com.portafolio.controlrecetamedica.application.auth.dto.VerifyRequest;
import com.portafolio.controlrecetamedica.domain.auth.port.AuthMailPort;
import com.portafolio.controlrecetamedica.domain.auth.port.OtpServicePort;
import com.portafolio.controlrecetamedica.domain.user.model.User;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class VerifyUserUseCase {

    private final UserRepositoryPort userRepo;
    private final OtpServicePort otpService;
    private final AuthMailPort authMailPort;

    public VerifyUserUseCase(
            UserRepositoryPort userRepo,
            OtpServicePort otpService,
            AuthMailPort authMailPort
    ) {
        this.userRepo = userRepo;
        this.otpService = otpService;
        this.authMailPort = authMailPort;
    }

    public MessageResponse execute(VerifyRequest req) {
        String email = req.getEmail().trim().toLowerCase();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        boolean ok = otpService.verifyOtp(email, req.getOtp());
        if (!ok) {
            throw new IllegalArgumentException("Código inválido o expirado");
        }

        User verifiedUser = new User(
                user.getId(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getRole(),
                true
        );
        userRepo.save(verifiedUser);

        authMailPort.sendWelcomeEmail(user.getEmail());

        return new MessageResponse("Cuenta verificada correctamente.");
    }
}