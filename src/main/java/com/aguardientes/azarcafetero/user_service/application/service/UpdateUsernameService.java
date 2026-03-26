package com.aguardientes.azarcafetero.user_service.application.service;

import com.aguardientes.azarcafetero.user_service.domain.model.*;
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

        if (!user.getLastNameChangedAt().canChangeName()) {
            long daysLeft = user.getLastNameChangedAt().daysUntilNextChange();
            return UsernameUpdateResult.cooldownActive(daysLeft);
        }

        if (!usernameAvailabilityPort.isAvailable(newUsername)) {
            return UsernameUpdateResult.nameTaken(
                    usernameAvailabilityPort.suggestAlternatives(newUsername)
            );
        }

        user.changeUsername(new Username(newUsername));
        profileRepository.save(user);
        return UsernameUpdateResult.ok();
    }
}
