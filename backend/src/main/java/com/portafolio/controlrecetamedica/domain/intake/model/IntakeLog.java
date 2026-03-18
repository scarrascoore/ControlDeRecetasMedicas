package com.portafolio.controlrecetamedica.domain.intake.model;

import java.time.Instant;

public class IntakeLog {

    private final Long id;
    private final Long scheduleId;

    private final Instant plannedAt;
    private final IntakeStatus status;

    private final Instant loggedAt;
    private final String note;

    public IntakeLog(Long id, Long scheduleId, Instant plannedAt, IntakeStatus status, Instant loggedAt, String note) {
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
    public IntakeStatus getStatus() { return status; }
    public Instant getLoggedAt() { return loggedAt; }
    public String getNote() { return note; }
}


