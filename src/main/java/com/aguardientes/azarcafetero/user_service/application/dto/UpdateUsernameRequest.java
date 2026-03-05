package com.aguardientes.azarcafetero.user_service.application.dto;

public class UpdateUsernameRequest {
    private String userId;
    private String newUsername;

    public UpdateUsernameRequest(String userId, String newUsername) {
        this.userId = userId;
        this.newUsername = newUsername;
    }

    public String getUserId() { return userId; }
    public String getNewUsername() { return newUsername; }
}
