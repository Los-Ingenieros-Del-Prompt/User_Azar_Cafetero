package com.aguardientes.azarcafetero.user_service.application.service;

import com.aguardientes.azarcafetero.user_service.domain.model.AvatarUrl;
import com.aguardientes.azarcafetero.user_service.domain.model.LastNameChangeDate;
import com.aguardientes.azarcafetero.user_service.domain.model.User;
import com.aguardientes.azarcafetero.user_service.domain.model.Username;
import com.aguardientes.azarcafetero.user_service.domain.port.in.UpdateAvatarUseCase;
import com.aguardientes.azarcafetero.user_service.domain.port.out.ProfileRepository;

public class UpdateAvatarService implements UpdateAvatarUseCase {

    private final ProfileRepository profileRepository;

    public UpdateAvatarService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public void execute(String userId, String newAvatarUrl) {
        User user = profileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User newUser = new User(
                            userId,
                            new Username("Player_" + userId.substring(0, 6)),
                            new AvatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=" + userId),
                            new LastNameChangeDate(null)
                    );
                    profileRepository.save(newUser);
                    return newUser;
                });

        user.changeAvatar(new AvatarUrl(newAvatarUrl));
        profileRepository.save(user);
    }
}
