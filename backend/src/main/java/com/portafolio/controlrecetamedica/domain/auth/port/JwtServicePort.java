package com.portafolio.controlrecetamedica.domain.auth.port;

public interface JwtServicePort {
    String generateToken(String email, String role);
    boolean isValid(String token);
    String extractEmail(String token);
    String extractRole(String token);
}

