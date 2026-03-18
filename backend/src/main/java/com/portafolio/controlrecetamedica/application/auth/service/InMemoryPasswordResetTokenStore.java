package com.portafolio.controlrecetamedica.application.auth.service;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryPasswordResetTokenStore {

    private static class Entry {
        final String email;
        final Instant expiresAt;

        Entry(String email, Instant expiresAt) {
            this.email = email;
            this.expiresAt = expiresAt;
        }
    }

    private final Map<String, Entry> tokens = new ConcurrentHashMap<>();
    private final Duration ttl = Duration.ofMinutes(15);

    public String create(String email) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, new Entry(email, Instant.now().plus(ttl)));
        return token;
    }

    public boolean isValid(String email, String token) {
        Entry e = tokens.get(token);
        if (e == null) return false;
        if (!e.email.equalsIgnoreCase(email)) return false;
        if (Instant.now().isAfter(e.expiresAt)) {
            tokens.remove(token);
            return false;
        }
        return true;
    }

    public void consume(String token) {
        tokens.remove(token);
    }
}
