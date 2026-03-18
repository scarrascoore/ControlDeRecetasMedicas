package com.portafolio.controlrecetamedica.application.intake.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MarkIntakeRequest {

    // "HH:mm"
    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "plannedTime debe ser HH:mm")
    private String plannedTime;

    @NotBlank
    private String status;

    @Size(max = 300)
    private String note;

    public String getPlannedTime() { return plannedTime; }
    public String getStatus() { return status; }
    public String getNote() { return note; }

    public void setPlannedTime(String plannedTime) { this.plannedTime = plannedTime; }
    public void setStatus(String status) { this.status = status; }
    public void setNote(String note) { this.note = note; }
}

