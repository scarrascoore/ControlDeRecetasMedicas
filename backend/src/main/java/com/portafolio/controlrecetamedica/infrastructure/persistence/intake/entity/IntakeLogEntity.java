package com.portafolio.controlrecetamedica.infrastructure.persistence.intake.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "intake_logs",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_intake_schedule_planned", columnNames = {"scheduleId", "plannedAt"})
        },
        indexes = {
                @Index(name = "idx_intake_schedule_id", columnList = "scheduleId"),
                @Index(name = "idx_intake_planned_at", columnList = "plannedAt")
        }
)
public class IntakeLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long scheduleId;

    @Column(nullable = false)
    private Instant plannedAt;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(nullable = false)
    private Instant loggedAt;

    @Column(length = 300)
    private String note;

    public Long getId() { return id; }
    public Long getScheduleId() { return scheduleId; }
    public Instant getPlannedAt() { return plannedAt; }
    public String getStatus() { return status; }
    public Instant getLoggedAt() { return loggedAt; }
    public String getNote() { return note; }

    public void setId(Long id) { this.id = id; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }
    public void setPlannedAt(Instant plannedAt) { this.plannedAt = plannedAt; }
    public void setStatus(String status) { this.status = status; }
    public void setLoggedAt(Instant loggedAt) { this.loggedAt = loggedAt; }
    public void setNote(String note) { this.note = note; }
}