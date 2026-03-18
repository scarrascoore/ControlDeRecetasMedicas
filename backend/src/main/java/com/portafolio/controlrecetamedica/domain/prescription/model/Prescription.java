package com.portafolio.controlrecetamedica.domain.prescription.model;

import java.time.Instant;

public class Prescription {
    private final Long id;
    private final Long userId;
    private final Long specialtyId;
    private final String title;
    private final String notes;
    private final Instant createdAt;

    public Prescription(Long id, Long userId, Long specialtyId, String title, String notes, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.specialtyId = specialtyId;
        this.title = title;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getSpecialtyId() { return specialtyId; }
    public String getTitle() { return title; }
    public String getNotes() { return notes; }
    public Instant getCreatedAt() { return createdAt; }
}
