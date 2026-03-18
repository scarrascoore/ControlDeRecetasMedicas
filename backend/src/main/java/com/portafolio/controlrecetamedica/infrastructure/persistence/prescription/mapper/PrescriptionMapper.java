package com.portafolio.controlrecetamedica.infrastructure.persistence.prescription.mapper;

import com.portafolio.controlrecetamedica.domain.prescription.model.Prescription;
import com.portafolio.controlrecetamedica.infrastructure.persistence.prescription.entity.PrescriptionEntity;

public class PrescriptionMapper {

    public static Prescription toDomain(PrescriptionEntity e) {
        return new Prescription(
                e.getId(),
                e.getUserId(),
                e.getSpecialtyId(),
                e.getTitle(),
                e.getNotes(),
                e.getCreatedAt()
        );
    }

    public static PrescriptionEntity toEntity(Prescription d) {
        PrescriptionEntity e = new PrescriptionEntity();
        e.setId(d.getId());
        e.setUserId(d.getUserId());
        e.setSpecialtyId(d.getSpecialtyId());
        e.setTitle(d.getTitle());
        e.setNotes(d.getNotes());
        e.setCreatedAt(d.getCreatedAt());
        return e;
    }
}
