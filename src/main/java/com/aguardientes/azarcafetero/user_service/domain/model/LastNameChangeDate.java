package com.aguardientes.azarcafetero.user_service.domain.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class LastNameChangeDate {

    private static final int COOLDOWN_DAYS = 30;
    private final LocalDateTime value;

    public LastNameChangeDate(LocalDateTime value) {
        this.value = value;
    }

    public boolean canChangeName() {
        if (value == null) return true;
        return ChronoUnit.DAYS.between(value, LocalDateTime.now()) >= COOLDOWN_DAYS;
    }

    public long daysUntilNextChange() {
        if (value == null) return 0;
        long elapsed = ChronoUnit.DAYS.between(value, LocalDateTime.now());
        return Math.max(0, COOLDOWN_DAYS - elapsed);
    }

    public LocalDateTime getValue() { return value; }
}