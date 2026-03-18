package com.portafolio.controlrecetamedica.application.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordResetConfirmRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String resetToken;

    @NotBlank
    @Size(min = 6, message = "newPassword debe tener al menos 6 caracteres")
    private String newPassword;

    public PasswordResetConfirmRequest() {}

    public String getEmail() { return email; }
    public String getResetToken() { return resetToken; }
    public String getNewPassword() { return newPassword; }

    public void setEmail(String email) { this.email = email; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}