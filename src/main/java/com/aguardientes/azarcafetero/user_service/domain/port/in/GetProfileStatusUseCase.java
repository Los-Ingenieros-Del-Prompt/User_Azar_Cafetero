package com.aguardientes.azarcafetero.user_service.domain.port.in;

import com.aguardientes.azarcafetero.user_service.domain.model.ProfileStatus;

public interface GetProfileStatusUseCase {
    ProfileStatus execute(String userId);
}
