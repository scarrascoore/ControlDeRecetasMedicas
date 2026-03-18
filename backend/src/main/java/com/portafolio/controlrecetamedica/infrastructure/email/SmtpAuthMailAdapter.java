package com.portafolio.controlrecetamedica.infrastructure.email;

import com.portafolio.controlrecetamedica.domain.auth.port.AuthMailPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SmtpAuthMailAdapter implements AuthMailPort {

    private final JavaMailSender mailSender;
    private final String from;

    public SmtpAuthMailAdapter(
            JavaMailSender mailSender,
            @Value("${spring.mail.username}") String from
    ) {
        this.mailSender = mailSender;
        this.from = from;
    }

    @Override
    public void sendOtpEmail(String to, String code) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject("Código de verificación");
        msg.setText(
                "Hola,\n\n" +
                        "Tu código de verificación es: " + code + "\n\n" +
                        "Ingresa este código en la pantalla de verificación para activar tu cuenta.\n\n" +
                        "Si no solicitaste este registro, ignora este mensaje."
        );
        mailSender.send(msg);
    }

    @Override
    public void sendWelcomeEmail(String to) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject("¡Bienvenido!");
        msg.setText(
                "Hola,\n\n" +
                        "Tu cuenta ya fue verificada correctamente.\n\n" +
                        "¡Bienvenido! Ya puedes iniciar sesión y empezar a usar el sistema de control de recetas médicas.\n\n" +
                        "Gracias por registrarte."
        );
        mailSender.send(msg);
    }

    // NUEVO: OTP para restablecer contraseña
    @Override
    public void sendPasswordResetOtpEmail(String to, String code) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject("Código para restablecer tu contraseña");
        msg.setText(
                "Hola,\n\n" +
                        "Recibimos una solicitud para restablecer tu contraseña.\n\n" +
                        "Tu código (OTP) es: " + code + "\n\n" +
                        "Si no fuiste tú, ignora este mensaje.\n"
        );
        mailSender.send(msg);
    }

    // NUEVO: confirmación de cambio de contraseña
    @Override
    public void sendPasswordChangedEmail(String to) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject("Tu contraseña fue actualizada");
        msg.setText(
                "Hola,\n\n" +
                        "Tu contraseña fue actualizada correctamente.\n\n" +
                        "Si no realizaste este cambio, revisa tu cuenta de inmediato.\n"
        );
        mailSender.send(msg);
    }
}