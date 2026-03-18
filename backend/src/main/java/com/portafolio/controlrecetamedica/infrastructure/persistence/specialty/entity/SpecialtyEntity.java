package com.portafolio.controlrecetamedica.infrastructure.persistence.specialty.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "specialties", uniqueConstraints = {
        @UniqueConstraint(name = "uk_specialty_name", columnNames = "name")
})
public class SpecialtyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false)
    private boolean active;

    public Long getId() { return id; }
    public String getName() { return name; }
    public boolean isActive() { return active; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setActive(boolean active) { this.active = active; }
}
