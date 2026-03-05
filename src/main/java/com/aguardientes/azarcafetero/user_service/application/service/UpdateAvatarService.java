package com.aguardientes.azarcafetero.user_service.application.service;

import com.aguardientes.azarcafetero.user_service.domain.model.AvatarUrl;
import com.aguardientes.azarcafetero.user_service.domain.model.User;
import com.aguardientes.azarcafetero.user_service.domain.port.in.UpdateAvatarUseCase;
import com.aguardientes.azarcafetero.user_service.domain.port.out.ProfileRepository;

public class UpdateAvatarService implements UpdateAvatarUseCase {

    private final ProfileRepository profileRepository;

    public UpdateAvatarService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public void execute(String userId, String newAvatarUrl) {
        // 1. Buscar usuario
        User user = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + userId));

        // 2. Delegar cambio al dominio
        user.changeAvatar(new AvatarUrl(newAvatarUrl));

        // 3. Persistir
        profileRepository.save(user);
    }
}
