package com.aguardientes.azarcafetero.user_service.infraestructure.adapter.out.sugestion;

import com.aguardientes.azarcafetero.user_service.domain.port.out.UsernameAvailabilityPort;
import com.aguardientes.azarcafetero.user_service.infraestructure.adapter.out.persistence.UserJpaRepository;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class UsernameAvailabilityAdapter implements UsernameAvailabilityPort {

    private final UserJpaRepository userJpaRepository;

    public UsernameAvailabilityAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public boolean isAvailable(String username) {
        return !userJpaRepository.existsByUsername(username);
    }

    @Override
    public List<String> suggestAlternatives(String username) {
        return List.of(
                username + "1",
                username + "_2",
                "El" + username,
                username + "_ok"
        );
    }
}