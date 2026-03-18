package com.portafolio.controlrecetamedica.infrastructure.persistence.prescription.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "prescriptions", indexes = {
        @Index(name = "idx_prescriptions_user_id", columnList = "userId"),
        @Index(name = "idx_prescriptions_specialty_id", columnList = "specialtyId")
})
public class PrescriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long specialtyId;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false)
    private Instant createdAt;

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getSpecialtyId() { return specialtyId; }
    public String getTitle() { return title; }
    public String getNotes() { return notes; }
    public Instant getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setSpecialtyId(Long specialtyId) { this.specialtyId = specialtyId; }
    public void setTitle(String title) { this.title = title; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
