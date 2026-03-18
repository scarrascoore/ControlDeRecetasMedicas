package com.portafolio.controlrecetamedica.application.auth.usecase;

import com.portafolio.controlrecetamedica.application.auth.dto.MessageResponse;
import com.portafolio.controlrecetamedica.application.auth.dto.PasswordResetRequest;
import com.portafolio.controlrecetamedica.domain.auth.port.AuthMailPort;
import com.portafolio.controlrecetamedica.domain.auth.port.OtpServicePort;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class RequestPasswordResetUseCase {

    private final UserRepositoryPort userRepo;
    private final OtpServicePort otpService;
    private final AuthMailPort authMailPort;

    public RequestPasswordResetUseCase(
            UserRepositoryPort userRepo,
            OtpServicePort otpService,
            AuthMailPort authMailPort
    ) {
        this.userRepo = userRepo;
        this.otpService = otpService;
        this.authMailPort = authMailPort;
    }

    public MessageResponse execute(PasswordResetRequest req) {
        String email = req.getEmail().trim().toLowerCase();

        userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        String otp = otpService.generateAndStoreOtp(email);
        authMailPort.sendPasswordResetOtpEmail(email, otp);

        return new MessageResponse("Se envió un código OTP al correo para restablecer la contraseña.");
    }
}