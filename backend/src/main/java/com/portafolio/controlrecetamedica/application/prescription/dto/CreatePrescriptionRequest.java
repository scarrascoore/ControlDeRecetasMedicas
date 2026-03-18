package com.portafolio.controlrecetamedica.application.prescription.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreatePrescriptionRequest {

    @NotNull
    private Long specialtyId;

    @NotBlank
    @Size(max = 120)
    private String title;

    @Size(max = 1000)
    private String notes;

    public Long getSpecialtyId() { return specialtyId; }
    public String getTitle() { return title; }
    public String getNotes() { return notes; }

    public void setSpecialtyId(Long specialtyId) { this.specialtyId = specialtyId; }
    public void setTitle(String title) { this.title = title; }
    public void setNotes(String notes) { this.notes = notes; }
}