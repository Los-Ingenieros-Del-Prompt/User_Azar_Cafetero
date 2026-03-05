package com.aguardientes.azarcafetero.user_service.domain.port.in;

import com.aguardientes.azarcafetero.user_service.application.dto.ProfileStatusResponse;

public interface GetProfileStatusUseCase {
    ProfileStatusResponse execute(String userId);
}
