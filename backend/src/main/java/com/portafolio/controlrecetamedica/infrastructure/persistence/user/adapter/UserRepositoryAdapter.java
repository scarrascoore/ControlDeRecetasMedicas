package com.portafolio.controlrecetamedica.infrastructure.persistence.user.adapter;

import com.portafolio.controlrecetamedica.domain.user.model.User;
import com.portafolio.controlrecetamedica.domain.user.repository.UserRepositoryPort;
import com.portafolio.controlrecetamedica.infrastructure.persistence.user.jpa.UserJpaRepository;
import com.portafolio.controlrecetamedica.infrastructure.persistence.user.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository jpa;

    public UserRepositoryAdapter(UserJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpa.findByEmail(email).map(UserMapper::toDomain);
    }

    @Override
    public User save(User user) {
        var saved = jpa.save(UserMapper.toEntity(user));
        return UserMapper.toDomain(saved);
    }
}