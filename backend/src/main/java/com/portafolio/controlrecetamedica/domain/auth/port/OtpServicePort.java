package com.portafolio.controlrecetamedica.domain.auth.port;

public interface OtpServicePort {
    String generateAndStoreOtp(String email);
    boolean verifyOtp(String email, String otp);
    String resendOtp(String email);
}

