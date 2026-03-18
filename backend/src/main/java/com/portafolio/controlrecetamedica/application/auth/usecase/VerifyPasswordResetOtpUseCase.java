package com.portafolio.controlrecetamedica.application.auth.usecase;

import com.portafolio.controlrecetamedica.application.auth.dto.PasswordResetVerifyRequest;
import com.portafolio.controlrecetamedica.application.auth.dto.PasswordResetVerifyResponse;
import com.portafolio.controlrecetamedica.application.auth.service.InMemoryPasswordResetTokenStore;
import com.portafolio.controlrecetamedica.domain.auth.port.OtpServicePort;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class VerifyPasswordResetOtpUseCase {

    private final UserRepositoryPort userRepo;
    private final OtpServicePort otpService;
    private final InMemoryPasswordResetTokenStore tokenStore;

    public VerifyPasswordResetOtpUseCase(
            UserRepositoryPort userRepo,
            OtpServicePort otpService,
            InMemoryPasswordResetTokenStore tokenStore
    ) {
        this.userRepo = userRepo;
        this.otpService = otpService;
        this.tokenStore = tokenStore;
    }

    public PasswordResetVerifyResponse execute(PasswordResetVerifyRequest req) {
        String email = req.getEmail().trim().toLowerCase();

        userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        boolean ok = otpService.verifyOtp(email, req.getOtp());
        if (!ok) throw new IllegalArgumentException("Código inválido o expirado");

        String resetToken = tokenStore.create(email);
        return new PasswordResetVerifyResponse("OTP verificado. Ya puedes ingresar tu nueva contraseña.", resetToken);
    }
}