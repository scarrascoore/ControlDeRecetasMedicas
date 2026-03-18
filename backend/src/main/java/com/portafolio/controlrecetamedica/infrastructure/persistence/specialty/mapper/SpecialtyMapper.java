package com.portafolio.controlrecetamedica.infrastructure.persistence.specialty.mapper;

import com.portafolio.controlrecetamedica.domain.specialty.model.Specialty;
import com.portafolio.controlrecetamedica.infrastructure.persistence.specialty.entity.SpecialtyEntity;

public class SpecialtyMapper {

    public static Specialty toDomain(SpecialtyEntity e) {
        return new Specialty(e.getId(), e.getName(), e.isActive());
    }

    public static SpecialtyEntity toEntity(Specialty d) {
        SpecialtyEntity e = new SpecialtyEntity();
        e.setId(d.getId());
        e.setName(d.getName());
        e.setActive(d.isActive());
        return e;
    }
}
