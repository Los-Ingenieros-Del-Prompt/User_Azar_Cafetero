package com.aguardientes.azarcafetero.user_service.application.service;

import com.aguardientes.azarcafetero.user_service.domain.model.User;
import com.aguardientes.azarcafetero.user_service.domain.model.Username;
import com.aguardientes.azarcafetero.user_service.domain.model.UsernameUpdateResult;
import com.aguardientes.azarcafetero.user_service.domain.port.in.UpdateUsernameUseCase;
import com.aguardientes.azarcafetero.user_service.domain.port.out.ProfileRepository;
import com.aguardientes.azarcafetero.user_service.domain.port.out.UsernameAvailabilityPort;

public class UpdateUsernameService implements UpdateUsernameUseCase {

    private final ProfileRepository profileRepository;
    private final UsernameAvailabilityPort usernameAvailabilityPort;

    public UpdateUsernameService(ProfileRepository profileRepository,
                                 UsernameAvailabilityPort usernameAvailabilityPort) {
        this.profileRepository = profileRepository;
        this.usernameAvailabilityPort = usernameAvailabilityPort;
    }

    @Override
    public UsernameUpdateResult execute(String userId, String newUsername) {
        // 1. Buscar usuario
        User user = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + userId));

        // 2. Verificar cooldown de 30 días (lógica en el dominio)
        if (!user.getLastNameChangedAt().canChangeName()) {
            long daysLeft = user.getLastNameChangedAt().daysUntilNextChange();
            return UsernameUpdateResult.cooldownActive(daysLeft);
        }

        // 3. Verificar unicidad del nombre
        if (!usernameAvailabilityPort.isAvailable(newUsername)) {
            return UsernameUpdateResult.nameTaken(
                    usernameAvailabilityPort.suggestAlternatives(newUsername)
            );
        }

        // 4. Aplicar cambio a través del dominio
        user.changeUsername(new Username(newUsername));

        // 5. Persistir
        profileRepository.save(user);

        return UsernameUpdateResult.ok();
    }
}
