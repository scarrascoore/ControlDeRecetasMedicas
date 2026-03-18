package com.portafolio.controlrecetamedica.application.auth.usecase;

import com.portafolio.controlrecetamedica.application.auth.dto.LoginRequest;
import com.portafolio.controlrecetamedica.application.auth.dto.LoginResponse;
import com.portafolio.controlrecetamedica.domain.auth.port.JwtServicePort;
import com.portafolio.controlrecetamedica.domain.auth.port.PasswordHasherPort;
import com.portafolio.controlrecetamedica.domain.user.model.User;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {

    private final UserRepositoryPort userRepo;
    private final PasswordHasherPort hasher;
    private final JwtServicePort jwt;

    public LoginUseCase(UserRepositoryPort userRepo, PasswordHasherPort hasher, JwtServicePort jwt) {
        this.userRepo = userRepo;
        this.hasher = hasher;
        this.jwt = jwt;
    }

    public LoginResponse execute(LoginRequest req) {
        String email = req.getEmail().toLowerCase();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        if (!user.isVerified()) {
            throw new IllegalArgumentException("Cuenta no verificada. Verifica tu correo con el código OTP.");
        }

        boolean ok = hasher.matches(req.getPassword(), user.getPasswordHash());
        if (!ok) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        String token = jwt.generateToken(user.getEmail(), user.getRole().name());

        return new LoginResponse("Login correcto", token);
    }
}

