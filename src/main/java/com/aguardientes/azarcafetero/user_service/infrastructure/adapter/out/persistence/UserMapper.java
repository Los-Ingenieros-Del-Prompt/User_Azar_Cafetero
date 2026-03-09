package com.aguardientes.azarcafetero.user_service.infrastructure.adapter.out.persistence;

import com.aguardientes.azarcafetero.user_service.domain.model.*;

public class UserMapper {

    // JPA Entity → Dominio
    public static User toDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                new Username(entity.getUsername()),
                new AvatarUrl(entity.getAvatarUrl()),
                new LastNameChangeDate(entity.getLastNameChangedAt())
        );
    }

    // Dominio → JPA Entity
    public static UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(
                user.getUserId(),
                user.getUsername().getValue(),
                user.getAvatarUrl().getValue(),
                user.getLastNameChangedAt() != null
                        ? user.getLastNameChangedAt().getValue()
                        : null
        );
    }
}
