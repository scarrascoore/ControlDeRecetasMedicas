package com.portafolio.controlrecetamedica.application.auth.dto;

public class PasswordResetVerifyResponse {

    private final String message;
    private final String resetToken;

    public PasswordResetVerifyResponse(String message, String resetToken) {
        this.message = message;
        this.resetToken = resetToken;
    }

    public String getMessage() { return message; }
    public String getResetToken() { return resetToken; }
}