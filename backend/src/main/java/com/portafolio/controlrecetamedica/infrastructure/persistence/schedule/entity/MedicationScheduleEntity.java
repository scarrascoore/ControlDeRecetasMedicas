package com.portafolio.controlrecetamedica.infrastructure.persistence.schedule.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "medication_schedules", indexes = {
        @Index(name = "idx_schedule_prescription_id", columnList = "prescriptionId")
})
public class MedicationScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private Long prescriptionId;

    @Column(nullable=false, length=160)
    private String medicineName;

    @Column(length=80)
    private String dose;

    @Column(nullable=false, length=40)
    private String frequencyType;

    private Integer timesPerDay;
    private Integer intervalHours;

    @Column(nullable=false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable=false)
    private boolean active;

    @Column(nullable=false)
    private LocalTime startTime;

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
    public LocalTime getStartTime() { return startTime; }

    public void setId(Long id) { this.id = id; }
    public void setPrescriptionId(Long prescriptionId) { this.prescriptionId = prescriptionId; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public void setDose(String dose) { this.dose = dose; }
    public void setFrequencyType(String frequencyType) { this.frequencyType = frequencyType; }
    public void setTimesPerDay(Integer timesPerDay) { this.timesPerDay = timesPerDay; }
    public void setIntervalHours(Integer intervalHours) { this.intervalHours = intervalHours; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setActive(boolean active) { this.active = active; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
}