package com.aguardientes.azarcafetero.user_service.application.service;

import com.aguardientes.azarcafetero.user_service.domain.model.*;
import com.aguardientes.azarcafetero.user_service.domain.port.out.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAvatarServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    private UpdateAvatarService service;

    @BeforeEach
    void setUp() {
        service = new UpdateAvatarService(profileRepository);
    }

    // ✅ Avatar se actualiza y se guarda
    @Test
    void whenValidUrl_thenAvatarIsUpdated() {
        User user = new User(
                "user-123",
                new Username("Jugador1"),
                new AvatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=old"),
                new LastNameChangeDate(null)
        );
        when(profileRepository.findByUserId("user-123")).thenReturn(Optional.of(user));

        String newAvatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=new";
        service.execute("user-123", newAvatar);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(profileRepository).save(captor.capture());

        assertThat(captor.getValue().getAvatarUrl().getValue()).isEqualTo(newAvatar);
    }

    // ✅ Usuario nuevo → se crea y luego se actualiza el avatar
    @Test
    void whenUserNotFound_thenCreatesUserAndUpdatesAvatar() {
        when(profileRepository.findByUserId("newuser-xyz")).thenReturn(Optional.empty());

        String newAvatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=fresh";
        service.execute("newuser-xyz", newAvatar);

        // Se llama save 2 veces: una al crear, otra al actualizar
        verify(profileRepository, times(2)).save(any(User.class));
    }

    // ✅ URL sin https → lanza excepción (validación del Value Object)
    @Test
    void whenUnsafeUrl_thenThrowsException() {
        User user = new User(
                "user-123",
                new Username("Jugador1"),
                new AvatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=safe"),
                new LastNameChangeDate(null)
        );
        when(profileRepository.findByUserId("user-123")).thenReturn(Optional.of(user));

        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> service.execute("user-123", "http://insecure.com/avatar.png")
        );
    }

    // ✅ El cambio de avatar no modifica el cooldown del nombre
    @Test
    void whenAvatarUpdated_thenCooldownIsUnaffected() {
        java.time.LocalDateTime fiveDaysAgo = java.time.LocalDateTime.now().minusDays(5);
        User user = new User(
                "user-123",
                new Username("Jugador1"),
                new AvatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=old"),
                new LastNameChangeDate(fiveDaysAgo)
        );
        when(profileRepository.findByUserId("user-123")).thenReturn(Optional.of(user));

        service.execute("user-123", "https://api.dicebear.com/7.x/avataaars/svg?seed=new");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(profileRepository).save(captor.capture());

        assertThat(captor.getValue().getLastNameChangedAt().canChangeName()).isFalse();
        assertThat(captor.getValue().getLastNameChangedAt().daysUntilNextChange()).isEqualTo(25);
    }
}