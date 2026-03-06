package com.aguardientes.azarcafetero.user_service.infraestructure.config;

import com.aguardientes.azarcafetero.user_service.application.service.*;
import com.aguardientes.azarcafetero.user_service.domain.port.out.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public UpdateAvatarService updateAvatarService(ProfileRepository profileRepository) {
        return new UpdateAvatarService(profileRepository);
    }

    @Bean
    public UpdateUsernameService updateUsernameService(ProfileRepository profileRepository,
                                                       UsernameAvailabilityPort usernameAvailabilityPort) {
        return new UpdateUsernameService(profileRepository, usernameAvailabilityPort);
    }

    @Bean
    public GetProfileStatusService getProfileStatusService(ProfileRepository profileRepository) {
        return new GetProfileStatusService(profileRepository);
    }
}
