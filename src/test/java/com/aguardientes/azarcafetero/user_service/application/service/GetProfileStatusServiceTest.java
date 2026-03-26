package com.aguardientes.azarcafetero.user_service.application.service;

import com.aguardientes.azarcafetero.user_service.application.dto.ProfileStatusResponse;
import com.aguardientes.azarcafetero.user_service.domain.model.*;
import com.aguardientes.azarcafetero.user_service.domain.port.out.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetProfileStatusServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    private GetProfileStatusService service;

    @BeforeEach
    void setUp() {
        service = new GetProfileStatusService(profileRepository);
    }

    // ✅ Usuario existente → retorna sus datos
    @Test
    void whenUserExists_thenReturnProfileData() {
        User user = new User(
                "user-123",
                new Username("JugadorTest"),
                new AvatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=test"),
                new LastNameChangeDate(null)
        );
        when(profileRepository.findByUserId("user-123")).thenReturn(Optional.of(user));

        ProfileStatusResponse response = service.execute("user-123");

        assertThat(response.getUsername()).isEqualTo("JugadorTest");
        assertThat(response.getAvatarUrl()).contains("dicebear");
        assertThat(response.isCanChangeName()).isTrue();
        assertThat(response.getDaysUntilNextChange()).isEqualTo(0);
    }

    // ✅ Usuario nuevo → se crea automáticamente (findOrCreate)
    @Test
    void whenUserNotFound_thenCreateAndReturnDefaultProfile() {
        when(profileRepository.findByUserId("newuser-abc")).thenReturn(Optional.empty());

        ProfileStatusResponse response = service.execute("newuser-abc");

        verify(profileRepository, times(1)).save(any(User.class));
        assertThat(response.getUsername()).isEqualTo("Player_newuse");
        assertThat(response.isCanChangeName()).isTrue();
    }

    // ✅ Cooldown activo → retorna días restantes correctamente
    @Test
    void whenCooldownActive_thenReturnCorrectDaysRemaining() {
        // Cambió el nombre hace 10 días → quedan 20
        LocalDateTime tenDaysAgo = LocalDateTime.now().minusDays(10);
        User user = new User(
                "user-456",
                new Username("CoolPlayer"),
                new AvatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=cool"),
                new LastNameChangeDate(tenDaysAgo)
        );
        when(profileRepository.findByUserId("user-456")).thenReturn(Optional.of(user));

        ProfileStatusResponse response = service.execute("user-456");

        assertThat(response.isCanChangeName()).isFalse();
        assertThat(response.getDaysUntilNextChange()).isEqualTo(20);
    }

    // ✅ Cooldown cumplido (30+ días) → puede cambiar nombre
    @Test
    void whenCooldownExpired_thenCanChangeName() {
        LocalDateTime thirtyOneDaysAgo = LocalDateTime.now().minusDays(31);
        User user = new User(
                "user-789",
                new Username("VetPlayer"),
                new AvatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=vet"),
                new LastNameChangeDate(thirtyOneDaysAgo)
        );
        when(profileRepository.findByUserId("user-789")).thenReturn(Optional.of(user));

        ProfileStatusResponse response = service.execute("user-789");

        assertThat(response.isCanChangeName()).isTrue();
        assertThat(response.getDaysUntilNextChange()).isEqualTo(0);
    }
}