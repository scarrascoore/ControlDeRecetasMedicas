package com.portafolio.controlrecetamedica.application.schedule.dto;

import jakarta.validation.constraints.*;

public class CreateMedicationScheduleRequest {

    @NotBlank
    @Size(max = 160)
    private String medicineName;

    @Size(max = 80)
    private String dose;

    @NotBlank
    private String frequencyType;

    @Min(1) @Max(6)
    private Integer timesPerDay;

    @Min(1) @Max(24)
    private Integer intervalHours;

    private String startDate;
    private String endDate;
    private Boolean active;
    private String startTime;

    public String getMedicineName() { return medicineName; }
    public String getDose() { return dose; }
    public String getFrequencyType() { return frequencyType; }
    public Integer getTimesPerDay() { return timesPerDay; }
    public Integer getIntervalHours() { return intervalHours; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public Boolean getActive() { return active; }
    public String getStartTime(){return startTime;}

    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public void setDose(String dose) { this.dose = dose; }
    public void setFrequencyType(String frequencyType) { this.frequencyType = frequencyType; }
    public void setTimesPerDay(Integer timesPerDay) { this.timesPerDay = timesPerDay; }
    public void setIntervalHours(Integer intervalHours) { this.intervalHours = intervalHours; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public void setActive(Boolean active) { this.active = active; }
    public void setStartTime(String startTime){this.startTime=startTime;}
}