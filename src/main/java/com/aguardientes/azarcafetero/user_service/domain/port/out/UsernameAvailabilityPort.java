package com.aguardientes.azarcafetero.user_service.domain.port.out;

import java.util.List;

public interface UsernameAvailabilityPort {
    boolean isAvailable(String username);
    List<String> suggestAlternatives(String username);
}