package com.portafolio.controlrecetamedica.infrastructure.web.controller;

import com.portafolio.controlrecetamedica.application.prescription.dto.CreatePrescriptionRequest;
import com.portafolio.controlrecetamedica.application.prescription.dto.PrescriptionResponse;
import com.portafolio.controlrecetamedica.application.prescription.usecase.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final CreatePrescriptionUseCase create;
    private final ListMyPrescriptionsUseCase listMine;
    private final GetPrescriptionUseCase getOne;
    private final DeletePrescriptionUseCase delete;

    public PrescriptionController(
            CreatePrescriptionUseCase create,
            ListMyPrescriptionsUseCase listMine,
            GetPrescriptionUseCase getOne,
            DeletePrescriptionUseCase delete
    ) {
        this.create = create;
        this.listMine = listMine;
        this.getOne = getOne;
        this.delete = delete;
    }

    @PostMapping
    public PrescriptionResponse create(Authentication auth, @Valid @RequestBody CreatePrescriptionRequest req) {
        return create.execute(auth.getName(), req);
    }

    @GetMapping("/mine")
    public List<PrescriptionResponse> mine(Authentication auth) {
        return listMine.execute(auth.getName());
    }

    @GetMapping("/{id}")
    public PrescriptionResponse get(Authentication auth, @PathVariable Long id) {
        return getOne.execute(auth.getName(), isAdmin(auth), id);
    }

    @DeleteMapping("/{id}")
    public void delete(Authentication auth, @PathVariable Long id) {
        delete.execute(auth.getName(), isAdmin(auth), id);
    }

    private boolean isAdmin(Authentication auth) {
        for (GrantedAuthority a : auth.getAuthorities()) {
            if ("ROLE_ADMIN".equals(a.getAuthority())) return true;
        }
        return false;
    }
}