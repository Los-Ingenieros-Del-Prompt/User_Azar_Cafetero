package com.aguardientes.azarcafetero.user_service.domain.port.out;

import com.aguardientes.azarcafetero.user_service.domain.model.User;
import java.util.Optional;

public interface ProfileRepository {
    Optional<User> findByUserId(String userId);
    void save(User user);
}