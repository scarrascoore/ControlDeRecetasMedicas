package com.portafolio.controlrecetamedica.domain.specialty.model;

public class Specialty {
    private final Long id;
    private final String name;
    private final boolean active;

    public Specialty(Long id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
}
