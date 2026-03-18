package com.portafolio.controlrecetamedica.infrastructure.web.controller;

import com.portafolio.controlrecetamedica.application.schedule.dto.CreateMedicationScheduleRequest;
import com.portafolio.controlrecetamedica.application.schedule.dto.MedicationScheduleResponse;
import com.portafolio.controlrecetamedica.application.schedule.usecase.CreateMedicationScheduleUseCase;
import com.portafolio.controlrecetamedica.application.schedule.usecase.ListSchedulesByPrescriptionUseCase;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescriptions/{prescriptionId}/schedules")
public class MedicationScheduleController {

    private final CreateMedicationScheduleUseCase create;
    private final ListSchedulesByPrescriptionUseCase list;

    public MedicationScheduleController(CreateMedicationScheduleUseCase create, ListSchedulesByPrescriptionUseCase list) {
        this.create = create;
        this.list = list;
    }

    @PostMapping
    public MedicationScheduleResponse create(Authentication auth,
                                             @PathVariable Long prescriptionId,
                                             @Valid @RequestBody CreateMedicationScheduleRequest req) {
        return create.execute(auth.getName(), isAdmin(auth), prescriptionId, req);
    }

    @GetMapping
    public List<MedicationScheduleResponse> list(Authentication auth,
                                                 @PathVariable Long prescriptionId) {
        return list.execute(auth.getName(), isAdmin(auth), prescriptionId);
    }

    private boolean isAdmin(Authentication auth) {
        for (GrantedAuthority a : auth.getAuthorities()) {
            if ("ROLE_ADMIN".equals(a.getAuthority())) return true;
        }
        return false;
    }
}