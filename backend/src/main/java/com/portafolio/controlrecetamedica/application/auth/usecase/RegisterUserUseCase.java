package com.portafolio.controlrecetamedica.application.auth.usecase;

import com.portafolio.controlrecetamedica.application.auth.dto.MessageResponse;
import com.portafolio.controlrecetamedica.application.auth.dto.RegisterRequest;
import com.portafolio.controlrecetamedica.domain.auth.port.AuthMailPort;
import com.portafolio.controlrecetamedica.domain.auth.port.OtpServicePort;
import com.portafolio.controlrecetamedica.domain.auth.port.PasswordHasherPort;
import com.portafolio.controlrecetamedica.domain.user.model.Role;
import com.portafolio.controlrecetamedica.domain.user.model.User;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserUseCase {

    private final UserRepositoryPort userRepo;
    private final PasswordHasherPort hasher;
    private final OtpServicePort otpService;
    private final AuthMailPort authMailPort;

    public RegisterUserUseCase(
            UserRepositoryPort userRepo,
            PasswordHasherPort hasher,
            OtpServicePort otpService,
            AuthMailPort authMailPort
    ) {
        this.userRepo = userRepo;
        this.hasher = hasher;
        this.otpService = otpService;
        this.authMailPort = authMailPort;
    }

    public MessageResponse execute(RegisterRequest req) {
        String email = req.getEmail().trim().toLowerCase();

        userRepo.findByEmail(email).ifPresent(u -> {
            throw new IllegalArgumentException("El email ya está registrado");
        });

        String passwordHash = hasher.hash(req.getPassword());

        User newUser = new User(null, email, passwordHash, Role.PACIENTE, false);
        userRepo.save(newUser);

        String otpCode = otpService.generateAndStoreOtp(email);

        authMailPort.sendOtpEmail(email, otpCode);

        return new MessageResponse("Usuario creado. Se envió un código de verificación.");
    }
}