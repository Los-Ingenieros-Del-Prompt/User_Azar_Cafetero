package com.aguardientes.azarcafetero.user_service.domain.port.in;

public interface UpdateAvatarUseCase {
    void execute(String userId, String newAvatarUrl);
}
