package com.portafolio.controlrecetamedica.application.specialty.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SpecialtyUpsertRequest {

    @NotBlank
    @Size(max = 120)
    private String name;

    private Boolean active;

    public String getName() { return name; }
    public Boolean getActive() { return active; }

    public void setName(String name) { this.name = name; }
    public void setActive(Boolean active) { this.active = active; }
}
