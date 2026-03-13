package com.aguardientes.azarcafetero.user_service.application.service;

import com.aguardientes.azarcafetero.user_service.domain.model.*;
import com.aguardientes.azarcafetero.user_service.domain.port.out.ProfileRepository;
import com.aguardientes.azarcafetero.user_service.domain.port.out.UsernameAvailabilityPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUsernameServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UsernameAvailabilityPort usernameAvailabilityPort;

    private UpdateUsernameService service;

    @BeforeEach
    void setUp() {
        service = new UpdateUsernameService(profileRepository, usernameAvailabilityPort);
    }

    // ✅ Nombre disponible → actualización exitosa
    @Test
    void whenUsernameAvailable_thenSuccess() {
        User user = new User(
                "user-123",
                new Username("OldName"),
                new AvatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=u1"),
                new LastNameChangeDate(null)
        );
        when(profileRepository.findByUserId("user-123")).thenReturn(Optional.of(user));
        when(usernameAvailabilityPort.isAvailable("NewName")).thenReturn(true);

        UsernameUpdateResult result = service.execute("user-123", "NewName");

        assertThat(result.isSuccess()).isTrue();
        verify(profileRepository).save(user);
    }

    // ✅ Nombre duplicado → retorna sugerencias válidas
    @Test
    void whenUsernameIsTaken_thenReturnsSuggestions() {
        User user = new User(
                "user-123",
                new Username("OldName"),
                new AvatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=u1"),
                new LastNameChangeDate(null)
        );
        when(profileRepository.findByUserId("user-123")).thenReturn(Optional.of(user));
        when(usernameAvailabilityPort.isAvailable("TakenName")).thenReturn(false);
        when(usernameAvailabilityPort.suggestAlternatives("TakenName"))
                .thenReturn(List.of("TakenName1", "TakenName_2", "ElTakenName"));

        UsernameUpdateResult result = service.execute("user-123", "TakenName");

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getSuggestions()).hasSize(3);
        assertThat(result.getSuggestions()).contains("TakenName1", "TakenName_2", "ElTakenName");
        verify(profileRepository, never()).save(any());
    }

    // ✅ Cooldown activo → retorna días restantes exactos
    @Test
    void whenCooldownActive_thenReturnExactDaysRemaining() {
        LocalDateTime fifteenDaysAgo = LocalDateTime.now().minusDays(15);
        User user = new User(
                "user-123",
                new Username("RecentChange"),
                new AvatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=rc"),
                new LastNameChangeDate(fifteenDaysAgo)
        );
        when(profileRepository.findByUserId("user-123")).thenReturn(Optional.of(user));

        UsernameUpdateResult result = service.execute("user-123", "AnyName");

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getDaysUntilNextChange()).isEqualTo(15);
        verify(usernameAvailabilityPort, never()).isAvailable(any());
        verify(profileRepository, never()).save(any());
    }

    // ✅ Cooldown a punto de expirar (día 29) → aún bloqueado
    @Test
    void whenCooldownOnDay29_thenStillBlocked() {
        LocalDateTime twentyNineDaysAgo = LocalDateTime.now().minusDays(29);
        User user = new User(
                "user-123",
                new Username("AlmostFree"),
                new AvatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=af"),
                new LastNameChangeDate(twentyNineDaysAgo)
        );
        when(profileRepository.findByUserId("user-123")).thenReturn(Optional.of(user));

        UsernameUpdateResult result = service.execute("user-123", "AnyName");

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getDaysUntilNextChange()).isEqualTo(1);
    }

    // ✅ Usuario nuevo → se crea y puede cambiar nombre
    @Test
    void whenNewUser_thenCreatesAndAllowsUsernameChange() {
        when(profileRepository.findByUserId("brandnew")).thenReturn(Optional.empty());        when(usernameAvailabilityPort.isAvailable("FreshName")).thenReturn(true);

        UsernameUpdateResult result = service.execute("brandnew", "FreshName");
        assertThat(result.isSuccess()).isTrue();
    }

    // ✅ Nombre cambiado exitosamente → cooldown se activa
    @Test
    void afterSuccessfulChange_thenCooldownActivates() {
        User user = new User(
                "user-123",
                new Username("BeforeName"),
                new AvatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=b"),
                new LastNameChangeDate(null)
        );
        when(profileRepository.findByUserId("user-123")).thenReturn(Optional.of(user));
        when(usernameAvailabilityPort.isAvailable("AfterName")).thenReturn(true);

        service.execute("user-123", "AfterName");

        // Después del cambio el cooldown debe estar activo
        assertThat(user.getLastNameChangedAt().canChangeName()).isFalse();
        assertThat(user.getLastNameChangedAt().daysUntilNextChange()).isEqualTo(30);
    }
}
