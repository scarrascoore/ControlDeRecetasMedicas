package com.portafolio.controlrecetamedica.application.auth.usecase;

import com.portafolio.controlrecetamedica.application.auth.dto.MessageResponse;
import com.portafolio.controlrecetamedica.domain.auth.port.OtpServicePort;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class ResendOtpUseCase {

    private final UserRepositoryPort userRepo;
    private final OtpServicePort otpService;

    public ResendOtpUseCase(UserRepositoryPort userRepo, OtpServicePort otpService) {
        this.userRepo = userRepo;
        this.otpService = otpService;
    }

    public MessageResponse execute(String emailRaw) {
        String email = emailRaw.toLowerCase();

        userRepo.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        otpService.resendOtp(email);

        return new MessageResponse("Se reenvió un nuevo código de verificación.");
    }
}
