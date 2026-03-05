package com.aguardientes.azarcafetero.user_service.application.dto;

public class ProfileStatusResponse {
    private String username;
    private String avatarUrl;
    private boolean canChangeName;
    private long daysUntilNextChange;

    public ProfileStatusResponse(String username, String avatarUrl,
                                 boolean canChangeName, long daysUntilNextChange) {
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
