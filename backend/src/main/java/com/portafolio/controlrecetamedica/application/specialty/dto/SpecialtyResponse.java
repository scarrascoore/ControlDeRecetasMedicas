package com.portafolio.controlrecetamedica.application.specialty.dto;

public class SpecialtyResponse {
    private final Long id;
    private final String name;
    private final boolean active;

    public SpecialtyResponse(Long id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
}
