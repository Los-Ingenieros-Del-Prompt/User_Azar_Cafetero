package com.aguardientes.azarcafetero.user_service.domain.model;

import java.util.List;

public class UsernameUpdateResult {

    private final boolean success;
    private final String message;
    private final List<String> suggestions;   // Solo si el nombre está en uso
    private final long daysUntilNextChange;   // Solo si está en cooldown

    // Constructor para éxito
    public static UsernameUpdateResult ok() {
        return new UsernameUpdateResult(true, "Nombre actualizado correctamente", null, 0);
    }

    // Constructor para nombre en uso
    public static UsernameUpdateResult nameTaken(List<String> suggestions) {
        return new UsernameUpdateResult(false, "El nombre ya está en uso", suggestions, 0);
    }

    // Constructor para cooldown activo
    public static UsernameUpdateResult cooldownActive(long days) {
        return new UsernameUpdateResult(false, "Debes esperar " + days + " días", null, days);
    }

    private UsernameUpdateResult(boolean success, String message, List<String> suggestions, long daysUntilNextChange) {
        this.success = success;
        this.message = message;
        this.suggestions = suggestions;
        this.daysUntilNextChange = daysUntilNextChange;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<String> getSuggestions() { return suggestions; }
    public long getDaysUntilNextChange() { return daysUntilNextChange; }
}

