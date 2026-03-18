package com.portafolio.controlrecetamedica.application.schedule.dto;

import java.time.LocalDate;

public class MedicationScheduleResponse {
    private final Long id;
    private final Long prescriptionId;
    private final String medicineName;
    private final String dose;
    private final String frequencyType;
    private final Integer timesPerDay;
    private final Integer intervalHours;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final boolean active;

    public MedicationScheduleResponse(Long id, Long prescriptionId, String medicineName, String dose,
                                      String frequencyType, Integer timesPerDay, Integer intervalHours,
                                      LocalDate startDate, LocalDate endDate, boolean active) {
        this.id = id;
        this.prescriptionId = prescriptionId;
        this.medicineName = medicineName;
        this.dose = dose;
        this.frequencyType = frequencyType;
        this.timesPerDay = timesPerDay;
        this.intervalHours = intervalHours;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }

    public Long getId() { return id; }
    public Long getPrescriptionId() { return prescriptionId; }
    public String getMedicineName() { return medicineName; }
    public String getDose() { return dose; }
    public String getFrequencyType() { return frequencyType; }
    public Integer getTimesPerDay() { return timesPerDay; }
    public Integer getIntervalHours() { return intervalHours; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public boolean isActive() { return active; }
}