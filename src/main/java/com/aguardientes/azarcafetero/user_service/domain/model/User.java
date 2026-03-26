package com.aguardientes.azarcafetero.user_service.domain.model;

import java.time.LocalDateTime;

public class User {

    private final String userId;
    private Username username;
    private AvatarUrl avatarUrl;
    private LastNameChangeDate lastNameChangedAt;

    public User(String userId, Username username, AvatarUrl avatarUrl, LastNameChangeDate lastNameChangedAt) {
        if (userId == null || userId.isBlank())
            throw new IllegalArgumentException("El userId no puede estar vacío");
        this.userId = userId;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.lastNameChangedAt = lastNameChangedAt;
    }

    // --- Comportamiento de dominio ---

    public void changeUsername(Username newUsername) {
        if (!lastNameChangedAt.canChangeName())
            throw new IllegalStateException(
                    "Debes esperar " + lastNameChangedAt.daysUntilNextChange() + " días para cambiar tu nombre"
            );
        this.username = newUsername;
        this.lastNameChangedAt = new LastNameChangeDate(LocalDateTime.now());
    }

    public void changeAvatar(AvatarUrl newAvatarUrl) {
        this.avatarUrl = newAvatarUrl;
    }

    public String getUserId() { return userId; }
    public Username getUsername() { return username; }
    public AvatarUrl getAvatarUrl() { return avatarUrl; }
    public LastNameChangeDate getLastNameChangedAt() { return lastNameChangedAt; }
}
