package com.aguardientes.azarcafetero.user_service.infrastructure.adapter.in.web;

import com.aguardientes.azarcafetero.user_service.application.dto.ProfileStatusResponse;
import com.aguardientes.azarcafetero.user_service.domain.model.UsernameUpdateResult;
import com.aguardientes.azarcafetero.user_service.domain.port.in.GetProfileStatusUseCase;
import com.aguardientes.azarcafetero.user_service.domain.port.in.UpdateAvatarUseCase;
import com.aguardientes.azarcafetero.user_service.domain.port.in.UpdateUsernameUseCase;
import com.aguardientes.azarcafetero.user_service.infrastructure.adapter.in.web.dto.UpdateAvatarRequest;
import com.aguardientes.azarcafetero.user_service.infrastructure.adapter.in.web.dto.UpdateUsernameRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final UpdateAvatarUseCase updateAvatarUseCase;
    private final UpdateUsernameUseCase updateUsernameUseCase;
    private final GetProfileStatusUseCase getProfileStatusUseCase;

    public ProfileController(UpdateAvatarUseCase updateAvatarUseCase,
                             UpdateUsernameUseCase updateUsernameUseCase,
                             GetProfileStatusUseCase getProfileStatusUseCase) {
        this.updateAvatarUseCase = updateAvatarUseCase;
        this.updateUsernameUseCase = updateUsernameUseCase;
        this.getProfileStatusUseCase = getProfileStatusUseCase;
    }

    // PUT /profile/avatar
    @PutMapping("/avatar")
    public ResponseEntity<?> updateAvatar(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody UpdateAvatarRequest request) {

        updateAvatarUseCase.execute(userId, request.getAvatarUrl());
        return ResponseEntity.ok(Map.of("message", "Avatar actualizado correctamente"));
    }

    // PUT /profile/username
    @PutMapping("/username")
    public ResponseEntity<?> updateUsername(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody UpdateUsernameRequest request) {

        UsernameUpdateResult result = updateUsernameUseCase.execute(userId, request.getUsername());

        if (result.isSuccess()) {
            return ResponseEntity.ok(Map.of("message", result.getMessage()));
        }

        // Nombre en uso → 409 Conflict
        if (result.getSuggestions() != null) {
            return ResponseEntity.status(409).body(Map.of(
                    "message", result.getMessage(),
                    "suggestions", result.getSuggestions()
            ));
        }

        // Cooldown activo → 403 Forbidden
        return ResponseEntity.status(403).body(Map.of(
                "message", result.getMessage(),
                "daysUntilNextChange", result.getDaysUntilNextChange()
        ));
    }

    // GET /profile/status
    @GetMapping("/status")
    public ResponseEntity<ProfileStatusResponse> getStatus(
            @RequestHeader("X-User-Id") String userId) {

        ProfileStatusResponse status = getProfileStatusUseCase.execute(userId);
        return ResponseEntity.ok(status);
    }
}