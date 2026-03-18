package com.portafolio.controlrecetamedica.domain.schedule.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class MedicationSchedule {

    private final Long id;
    private final Long prescriptionId;

    private final String medicineName;
    private final String dose;

    private final FrequencyType frequencyType;

    private final Integer timesPerDay;
    private final Integer intervalHours;

    private final LocalDate startDate;
    private final LocalDate endDate;

    private final boolean active;

    private final LocalTime startTime;

    public MedicationSchedule(
            Long id,
            Long prescriptionId,
            String medicineName,
            String dose,
            FrequencyType frequencyType,
            Integer timesPerDay,
            Integer intervalHours,
            LocalDate startDate,
            LocalDate endDate,
            boolean active,
            LocalTime startTime
    ) {
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
        this.startTime = startTime;
    }

    public Long getId() { return id; }
    public Long getPrescriptionId() { return prescriptionId; }
    public String getMedicineName() { return medicineName; }
    public String getDose() { return dose; }
    public FrequencyType getFrequencyType() { return frequencyType; }
    public Integer getTimesPerDay() { return timesPerDay; }
    public Integer getIntervalHours() { return intervalHours; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public boolean isActive() { return active; }
    public LocalTime getStartTime() {return startTime;}
}