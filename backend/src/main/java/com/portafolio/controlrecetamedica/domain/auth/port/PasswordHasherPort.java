package com.portafolio.controlrecetamedica.domain.auth.port;

public interface PasswordHasherPort {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String passwordHash);
}