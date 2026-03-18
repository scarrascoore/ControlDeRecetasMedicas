package com.portafolio.controlrecetamedica.domain.user.model;

public class User {
    private final Long id;
    private final String email;
    private final String passwordHash;
    private final Role role;
    private final boolean verified;

    public User(Long id, String email, String passwordHash, Role role, boolean verified) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.verified = verified;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public boolean isVerified() { return verified; }
}

