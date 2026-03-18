package com.portafolio.controlrecetamedica.application.intake.dto;

import java.time.Instant;

public class IntakeLogResponse {
    private final Long id;
    private final Long scheduleId;
    private final Instant plannedAt;
    private final String status;
    private final Instant loggedAt;
    private final String note;

    public IntakeLogResponse(Long id, Long scheduleId, Instant plannedAt, String status, Instant loggedAt, String note) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.plannedAt = plannedAt;
        this.status = status;
        this.loggedAt = loggedAt;
        this.note = note;
    }

    public Long getId() { return id; }
    public Long getScheduleId() { return scheduleId; }
    public Instant getPlannedAt() { return plannedAt; }
    public String getStatus() { return status; }
    public Instant getLoggedAt() { return loggedAt; }
    public String getNote() { return note; }
}

