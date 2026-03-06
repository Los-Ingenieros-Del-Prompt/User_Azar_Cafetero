package com.aguardientes.azarcafetero.user_service.domain.model;

public class ProfileStatus {

    private final String username;
    private final String avatarUrl;
    private final boolean canChangeName;
    private final long daysUntilNextChange;

    public ProfileStatus(String username, String avatarUrl, boolean canChangeName, long daysUntilNextChange) {
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.canChangeName = canChangeName;
        this.daysUntilNextChange = daysUntilNextChange;
    }

    public String getUsername() { return username; }
    public String getAvatarUrl() { return avatarUrl; }
    public boolean isCanChangeName() { return canChangeName; }
    public long getDaysUntilNextChange() { return daysUntilNextChange; }
}
