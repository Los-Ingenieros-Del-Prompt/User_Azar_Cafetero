package com.aguardientes.azarcafetero.user_service.application.service;

import com.aguardientes.azarcafetero.user_service.application.dto.ProfileStatusResponse;
import com.aguardientes.azarcafetero.user_service.domain.model.AvatarUrl;
import com.aguardientes.azarcafetero.user_service.domain.model.LastNameChangeDate;
import com.aguardientes.azarcafetero.user_service.domain.model.User;
import com.aguardientes.azarcafetero.user_service.domain.model.Username;
import com.aguardientes.azarcafetero.user_service.domain.port.in.GetProfileStatusUseCase;
import com.aguardientes.azarcafetero.user_service.domain.port.out.ProfileRepository;

public class GetProfileStatusService implements GetProfileStatusUseCase {

    private final ProfileRepository profileRepository;

    public GetProfileStatusService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public ProfileStatusResponse execute(String userId) {
        User user = profileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    // Crear usuario por defecto si no existe
                    User newUser = new User(
                            userId,
                            new Username("Player_" + userId.substring(0, 6)),
                            new AvatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=" + userId),
                            new LastNameChangeDate(null)
                    );
                    profileRepository.save(newUser);
                    return newUser;
                });

        return new ProfileStatusResponse(
                user.getUsername().getValue(),
                user.getAvatarUrl().getValue(),
                user.getLastNameChangedAt().canChangeName(),
                user.getLastNameChangedAt().daysUntilNextChange()
        );
    }
}
