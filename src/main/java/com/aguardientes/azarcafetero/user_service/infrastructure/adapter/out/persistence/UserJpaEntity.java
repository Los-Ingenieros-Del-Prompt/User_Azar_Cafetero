package com.aguardientes.azarcafetero.user_service.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "last_name_changed_at")
    private LocalDateTime lastNameChangedAt;

    // Constructor vacío requerido por JPA
    public UserJpaEntity() {}

    public UserJpaEntity(String id, String username, String avatarUrl, LocalDateTime lastNameChangedAt) {
        this.id = id;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.lastNameChangedAt = lastNameChangedAt;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public LocalDateTime getLastNameChangedAt() { return lastNameChangedAt; }
    public void setLastNameChangedAt(LocalDateTime lastNameChangedAt) {
        this.lastNameChangedAt = lastNameChangedAt;
    }
}
