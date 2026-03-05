package com.aguardientes.azarcafetero.user_service.application.dto;

public class UpdateAvatarRequest {
    private String userId;
    private String newAvatarUrl;

    public UpdateAvatarRequest(String userId, String newAvatarUrl) {
        this.userId = userId;
        this.newAvatarUrl = newAvatarUrl;
    }

    public String getUserId() { return userId; }
    public String getNewAvatarUrl() { return newAvatarUrl; }
}
