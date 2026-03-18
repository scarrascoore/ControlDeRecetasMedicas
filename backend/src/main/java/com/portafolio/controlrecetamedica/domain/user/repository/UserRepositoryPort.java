package com.portafolio.controlrecetamedica.domain.user.repository;

import com.portafolio.controlrecetamedica.domain.user.model.User;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByEmail(String email);
    User save(User user);
}


