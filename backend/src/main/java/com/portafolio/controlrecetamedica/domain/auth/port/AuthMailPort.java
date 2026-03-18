package com.portafolio.controlrecetamedica.domain.auth.port;

public interface AuthMailPort {
    void sendOtpEmail(String to, String code);
    void sendWelcomeEmail(String to);

    void sendPasswordResetOtpEmail(String to, String code);
    void sendPasswordChangedEmail(String to);
}