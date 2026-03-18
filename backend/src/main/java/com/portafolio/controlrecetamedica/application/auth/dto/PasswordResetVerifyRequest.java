package com.portafolio.controlrecetamedica.application.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PasswordResetVerifyRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\d{6}$", message = "otp debe tener exactamente 6 dígitos")
    private String otp;

    public PasswordResetVerifyRequest() {}

    public PasswordResetVerifyRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    public String getEmail() { return email; }
    public String getOtp() { return otp; }

    public void setEmail(String email) { this.email = email; }
    public void setOtp(String otp) { this.otp = otp; }
}