package com.portafolio.controlrecetamedica.infrastructure.web.controller;

import com.portafolio.controlrecetamedica.application.specialty.dto.SpecialtyResponse;
import com.portafolio.controlrecetamedica.application.specialty.dto.SpecialtyUpsertRequest;
import com.portafolio.controlrecetamedica.application.specialty.usecase.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/specialties")
public class SpecialtyController {

    private final ListSpecialtiesUseCase list;
    private final CreateSpecialtyUseCase create;
    private final UpdateSpecialtyUseCase update;
    private final DeleteSpecialtyUseCase delete;

    public SpecialtyController(
            ListSpecialtiesUseCase list,
            CreateSpecialtyUseCase create,
            UpdateSpecialtyUseCase update,
            DeleteSpecialtyUseCase delete
    ) {
        this.list = list;
        this.create = create;
        this.update = update;
        this.delete = delete;
    }

    @GetMapping
    public List<SpecialtyResponse> list() {
        return list.execute();
    }

    @PostMapping
    public SpecialtyResponse create(@Valid @RequestBody SpecialtyUpsertRequest req) {
        return create.execute(req);
    }

    @PutMapping("/{id}")
    public SpecialtyResponse update(@PathVariable Long id, @Valid @RequestBody SpecialtyUpsertRequest req) {
        return update.execute(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        delete.execute(id);
    }
}