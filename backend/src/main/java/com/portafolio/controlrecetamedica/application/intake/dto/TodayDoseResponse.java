package com.portafolio.controlrecetamedica.application.intake.dto;

import java.time.Instant;
import java.time.LocalTime;

public class TodayDoseResponse {

    private final Long scheduleId;
    private final String medicineName;
    private final String dose;

    private final LocalTime plannedTime;
    private final Instant plannedAt;

    private final String status;
    private final Instant loggedAt;
    private final String note;

    public TodayDoseResponse(
            Long scheduleId,
            String medicineName,
            String dose,
            LocalTime plannedTime,
            Instant plannedAt,
            String status,
            Instant loggedAt,
            String note
    ) {
        this.scheduleId = scheduleId;
        this.medicineName = medicineName;
        this.dose = dose;
        this.plannedTime = plannedTime;
        this.plannedAt = plannedAt;
        this.status = status;
        this.loggedAt = loggedAt;
        this.note = note;
    }

    public Long getScheduleId() { return scheduleId; }
    public String getMedicineName() { return medicineName; }
    public String getDose() { return dose; }
    public LocalTime getPlannedTime() { return plannedTime; }
    public Instant getPlannedAt() { return plannedAt; }
    public String getStatus() { return status; }
    public Instant getLoggedAt() { return loggedAt; }
    public String getNote() { return note; }

    public boolean isTaken() {
        return "TAKEN".equals(status);
    }
}