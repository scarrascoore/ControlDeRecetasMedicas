package com.portafolio.controlrecetamedica.application.intake.usecase;

import com.portafolio.controlrecetamedica.application.intake.dto.TodayDoseResponse;
import com.portafolio.controlrecetamedica.domain.intake.model.IntakeLog;
import com.portafolio.controlrecetamedica.domain.schedule.model.FrequencyType;
import com.portafolio.controlrecetamedica.domain.schedule.model.MedicationSchedule;
import com.portafolio.controlrecetamedica.domain.intake.repository.IntakeLogRepositoryPort;
import com.portafolio.controlrecetamedica.domain.prescription.repository.PrescriptionRepositoryPort;
import com.portafolio.controlrecetamedica.domain.schedule.repository.MedicationScheduleRepositoryPort;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GetTodayDosesUseCase {

    private final MedicationScheduleRepositoryPort scheduleRepo;
    private final PrescriptionRepositoryPort prescriptionRepo;
    private final UserRepositoryPort userRepo;
    private final IntakeLogRepositoryPort intakeRepo;

    private final ZoneId zone = ZoneId.of("America/Lima");

    public GetTodayDosesUseCase(
            MedicationScheduleRepositoryPort scheduleRepo,
            PrescriptionRepositoryPort prescriptionRepo,
            UserRepositoryPort userRepo,
            IntakeLogRepositoryPort intakeRepo
    ) {
        this.scheduleRepo = scheduleRepo;
        this.prescriptionRepo = prescriptionRepo;
        this.userRepo = userRepo;
        this.intakeRepo = intakeRepo;
    }

    public List<TodayDoseResponse> execute(String email) {
        var user = userRepo.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + email));

        LocalDate today = LocalDate.now(zone);
        Instant from = today.atStartOfDay(zone).toInstant();
        Instant to = today.plusDays(1).atStartOfDay(zone).toInstant();

        var prescriptions = prescriptionRepo.findByUserId(user.getId());
        if (prescriptions.isEmpty()) return List.of();

        List<MedicationSchedule> schedules = prescriptions.stream()
                .flatMap(p -> scheduleRepo.findByPrescriptionId(p.getId()).stream())
                .filter(MedicationSchedule::isActive)
                .filter(s -> isScheduleActiveToday(s, today))
                .filter(this::isSupportedTodaySchedule)
                .collect(Collectors.toList());

        if (schedules.isEmpty()) return List.of();

        List<TodayDoseResponse> out = new ArrayList<>();

        for (MedicationSchedule schedule : schedules) {
            Map<LocalTime, IntakeLog> logsByTime = intakeRepo
                    .findByScheduleIdBetween(schedule.getId(), from, to)
                    .stream()
                    .collect(Collectors.toMap(
                            log -> ZonedDateTime.ofInstant(log.getPlannedAt(), zone)
                                    .toLocalTime()
                                    .withSecond(0)
                                    .withNano(0),
                            log -> log,
                            (a, b) -> a
                    ));

            List<LocalTime> slots = buildSlots(schedule);

            for (LocalTime slot : slots) {
                Instant plannedAt = today.atTime(slot).atZone(zone).toInstant();

                IntakeLog log = logsByTime.get(slot.withSecond(0).withNano(0));

                String status = (log == null) ? "PENDING" : log.getStatus().name();
                Instant loggedAt = (log == null) ? null : log.getLoggedAt();
                String note = (log == null) ? null : log.getNote();

                out.add(new TodayDoseResponse(
                        schedule.getId(),
                        schedule.getMedicineName(),
                        schedule.getDose(),
                        slot,
                        plannedAt,
                        status,
                        loggedAt,
                        note
                ));
            }
        }

        out.sort(Comparator
                .comparing(TodayDoseResponse::getPlannedTime)
                .thenComparing(TodayDoseResponse::getMedicineName, Comparator.nullsLast(String::compareToIgnoreCase)));

        return out;
    }

    private boolean isScheduleActiveToday(MedicationSchedule s, LocalDate today) {
        LocalDate start = s.getStartDate();
        LocalDate end = s.getEndDate();

        if (start != null && today.isBefore(start)) return false;
        if (end != null && today.isAfter(end)) return false;

        return true;
    }

    private boolean isSupportedTodaySchedule(MedicationSchedule schedule) {
        return schedule.getFrequencyType() == FrequencyType.DAILY
                && schedule.getStartTime() != null
                && schedule.getTimesPerDay() != null
                && schedule.getTimesPerDay() > 0;
    }

    private List<LocalTime> buildSlots(MedicationSchedule schedule) {
        int timesPerDay = schedule.getTimesPerDay();
        int interval = Math.max(1, 24 / timesPerDay);

        List<LocalTime> slots = new ArrayList<>(timesPerDay);
        LocalTime base = schedule.getStartTime().withSecond(0).withNano(0);

        for (int i = 0; i < timesPerDay; i++) {
            slots.add(base.plusHours((long) i * interval));
        }

        return slots;
    }
}