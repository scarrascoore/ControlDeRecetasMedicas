package com.portafolio.controlrecetamedica.infrastructure.security.otp;

import com.portafolio.controlrecetamedica.domain.auth.port.OtpServicePort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryOtpService implements OtpServicePort {

    private static final int OTP_LENGTH = 6;
    private static final long EXPIRATION_SECONDS = 10 * 60;
    private static final int MAX_ATTEMPTS = 5;

    private final SecureRandom random = new SecureRandom();
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final Map<String, OtpRecord> store = new ConcurrentHashMap<>();

    @Override
    public String generateAndStoreOtp(String email) {
        String otp = generateOtp();
        store.put(email.toLowerCase(), new OtpRecord(
                encoder.encode(otp),
                Instant.now().plusSeconds(EXPIRATION_SECONDS),
                0,
                false
        ));

        System.out.println("OTP para " + email + ": " + otp + " (expira en 10 min)");

        return otp;
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        String key = email.toLowerCase();
        OtpRecord record = store.get(key);

        if (record == null) return false;
        if (record.used) return false;
        if (Instant.now().isAfter(record.expiresAt)) return false;
        if (record.attempts >= MAX_ATTEMPTS) return false;

        boolean ok = encoder.matches(otp, record.otpHash);
        if (ok) {
            record.used = true;
            store.put(key, record);
            return true;
        } else {
            record.attempts++;
            store.put(key, record);
            return false;
        }
    }

    @Override
    public String resendOtp(String email) {
        return generateAndStoreOtp(email);
    }

    private String generateOtp() {
        int bound = (int) Math.pow(10, OTP_LENGTH);
        int code = random.nextInt(bound);
        return String.format("%0" + OTP_LENGTH + "d", code);
    }

    private static class OtpRecord {
        final String otpHash;
        final Instant expiresAt;
        int attempts;
        boolean used;

        OtpRecord(String otpHash, Instant expiresAt, int attempts, boolean used) {
            this.otpHash = otpHash;
            this.expiresAt = expiresAt;
            this.attempts = attempts;
            this.used = used;
        }
    }
}
