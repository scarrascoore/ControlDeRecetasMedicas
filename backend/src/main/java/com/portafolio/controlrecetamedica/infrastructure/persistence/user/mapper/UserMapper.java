package com.portafolio.controlrecetamedica.infrastructure.persistence.user.mapper;

import com.portafolio.controlrecetamedica.domain.user.model.Role;
import com.portafolio.controlrecetamedica.domain.user.model.User;
import com.portafolio.controlrecetamedica.infrastructure.persistence.user.entity.UserEntity;

public class UserMapper {

    public static User toDomain(UserEntity e) {
        return new User(e.getId(), e.getEmail(), e.getPasswordHash(), Role.valueOf(e.getRole()), e.isVerified());
    }

    public static UserEntity toEntity(User d) {
        UserEntity e = new UserEntity();
        e.setId(d.getId());
        e.setEmail(d.getEmail());
        e.setPasswordHash(d.getPasswordHash());
        e.setRole(d.getRole().name());
        e.setVerified(d.isVerified());
        return e;
    }
}

