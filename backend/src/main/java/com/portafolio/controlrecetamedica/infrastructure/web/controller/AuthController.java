package com.portafolio.controlrecetamedica.infrastructure.web.controller;

import com.portafolio.controlrecetamedica.application.auth.dto.*;
import com.portafolio.controlrecetamedica.application.auth.usecase.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserUseCase register;
    private final VerifyUserUseCase verify;
    private final LoginUseCase login;
    private final ResendOtpUseCase resend;

    private final RequestPasswordResetUseCase requestPasswordReset;
    private final VerifyPasswordResetOtpUseCase verifyPasswordResetOtp;
    private final ConfirmPasswordResetUseCase confirmPasswordReset;

    public AuthController(
            RegisterUserUseCase register,
            VerifyUserUseCase verify,
            LoginUseCase login,
            ResendOtpUseCase resend,

            RequestPasswordResetUseCase requestPasswordReset,
            VerifyPasswordResetOtpUseCase verifyPasswordResetOtp,
            ConfirmPasswordResetUseCase confirmPasswordReset
    ) {
        this.register = register;
        this.verify = verify;
        this.login = login;
        this.resend = resend;

        this.requestPasswordReset = requestPasswordReset;
        this.verifyPasswordResetOtp = verifyPasswordResetOtp;
        this.confirmPasswordReset = confirmPasswordReset;
    }

    @PostMapping("/register")
    public MessageResponse register(@Valid @RequestBody RegisterRequest req) {
        return register.execute(req);
    }

    @PostMapping("/verify")
    public MessageResponse verify(@Valid @RequestBody VerifyRequest req) {
        return verify.execute(req);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        return login.execute(req);
    }

    @PostMapping("/resend-code")
    public MessageResponse resendCode(@RequestParam String email) {
        return resend.execute(email);
    }

    //RESET PASSWORD: 1) solicitar OTP al correo
    @PostMapping("/password-reset/request")
    public MessageResponse requestPasswordReset(@Valid @RequestBody PasswordResetRequest req) {
        return requestPasswordReset.execute(req);
    }

    //RESET PASSWORD: 2) verificar OTP y devolver resetToken
    @PostMapping("/password-reset/verify")
    public PasswordResetVerifyResponse verifyPasswordResetOtp(@Valid @RequestBody PasswordResetVerifyRequest req) {
        return verifyPasswordResetOtp.execute(req);
    }

    //RESET PASSWORD: 3) confirmar nueva contraseña usando resetToken
    @PostMapping("/password-reset/confirm")
    public MessageResponse confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmRequest req) {
        return confirmPasswordReset.execute(req);
    }
}
