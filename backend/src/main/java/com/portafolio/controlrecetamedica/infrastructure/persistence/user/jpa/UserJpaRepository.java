package com.portafolio.controlrecetamedica.infrastructure.persistence.user.jpa;

import com.portafolio.controlrecetamedica.infrastructure.persistence.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}