package com.aguardientes.azarcafetero.user_service.application.service;

import com.aguardientes.azarcafetero.user_service.application.dto.ProfileStatusResponse;
import com.aguardientes.azarcafetero.user_service.domain.model.User;
import com.aguardientes.azarcafetero.user_service.domain.port.in.GetProfileStatusUseCase;
import com.aguardientes.azarcafetero.user_service.domain.port.out.ProfileRepository;

public class GetProfileStatusService implements GetProfileStatusUseCase {

    private final ProfileRepository profileRepository;

    public GetProfileStatusService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public ProfileStatusResponse execute(String userId) {
        // 1. Buscar usuario
        User user = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + userId));

        // 2. Consultar estado del cooldown desde el dominio
        boolean canChange = user.getLastNameChangedAt().canChangeName();
        long daysLeft = user.getLastNameChangedAt().daysUntilNextChange();

        // 3. Retornar DTO con el estado
        return new ProfileStatusResponse(
                user.getUsername().getValue(),
                user.getAvatarUrl().getValue(),
                canChange,
                daysLeft
        );
    }
}
