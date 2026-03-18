package com.portafolio.controlrecetamedica.application.prescription.dto;

import java.time.Instant;

public class PrescriptionResponse {
    private final Long id;
    private final Long specialtyId;
    private final String specialtyName;
    private final String title;
    private final String notes;
    private final Instant createdAt;

    public PrescriptionResponse(Long id, Long specialtyId, String specialtyName, String title, String notes, Instant createdAt) {
        this.id = id;
        this.specialtyId = specialtyId;
        this.specialtyName = specialtyName;
        this.title = title;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getSpecialtyId() { return specialtyId; }
    public String getSpecialtyName() { return specialtyName; }
    public String getTitle() { return title; }
    public String getNotes() { return notes; }
    public Instant getCreatedAt() { return createdAt; }
}