package com.aguardientes.azarcafetero.user_service.domain.port.in;

import com.aguardientes.azarcafetero.user_service.domain.model.UsernameUpdateResult;

public interface UpdateUsernameUseCase {
    UsernameUpdateResult execute(String userId, String newUsername);
}
