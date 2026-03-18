package com.portafolio.controlrecetamedica.infrastructure.web.controller;

import com.portafolio.controlrecetamedica.application.intake.dto.IntakeLogResponse;
import com.portafolio.controlrecetamedica.application.intake.dto.MarkIntakeRequest;
import com.portafolio.controlrecetamedica.application.intake.dto.TodayDoseResponse;
import com.portafolio.controlrecetamedica.application.intake.usecase.GetTodayDosesUseCase;
import com.portafolio.controlrecetamedica.application.intake.usecase.ListIntakesByScheduleUseCase;
import com.portafolio.controlrecetamedica.application.intake.usecase.MarkIntakeUseCase;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class IntakeController {

    private final MarkIntakeUseCase mark;
    private final ListIntakesByScheduleUseCase listBySchedule;
    private final GetTodayDosesUseCase today;

    public IntakeController(MarkIntakeUseCase mark, ListIntakesByScheduleUseCase listBySchedule, GetTodayDosesUseCase today) {
        this.mark = mark;
        this.listBySchedule = listBySchedule;
        this.today = today;
    }

    @PostMapping("/schedules/{scheduleId}/intakes")
    public IntakeLogResponse markIntake(Authentication auth, @PathVariable Long scheduleId, @Valid @RequestBody MarkIntakeRequest req) {
        return mark.execute(auth.getName(), isAdmin(auth), scheduleId, req);
    }

    @GetMapping("/schedules/{scheduleId}/intakes")
    public List<IntakeLogResponse> listIntakes(Authentication auth,
                                               @PathVariable Long scheduleId,
                                               @RequestParam(required = false) String date) {
        LocalDate d = (date == null || date.isBlank()) ? LocalDate.now() : LocalDate.parse(date);
        return listBySchedule.execute(auth.getName(), isAdmin(auth), scheduleId, d);
    }

    @GetMapping("/today")
    public List<TodayDoseResponse> today(Authentication auth) {
        return today.execute(auth.getName());
    }

    private boolean isAdmin(Authentication auth) {
        for (GrantedAuthority a : auth.getAuthorities()) {
            if ("ROLE_ADMIN".equals(a.getAuthority())) return true;
        }
        return false;
    }
}