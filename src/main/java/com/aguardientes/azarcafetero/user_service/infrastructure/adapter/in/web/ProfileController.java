package com.aguardientes.azarcafetero.user_service.infrastructure.adapter.in.web;

import com.aguardientes.azarcafetero.user_service.application.dto.ProfileStatusResponse;
import com.aguardientes.azarcafetero.user_service.domain.model.UsernameUpdateResult;
import com.aguardientes.azarcafetero.user_service.domain.port.in.GetProfileStatusUseCase;
import com.aguardientes.azarcafetero.user_service.domain.port.in.UpdateAvatarUseCase;
import com.aguardientes.azarcafetero.user_service.domain.port.in.UpdateUsernameUseCase;
import com.aguardientes.azarcafetero.user_service.infrastructure.adapter.in.web.dto.UpdateAvatarRequest;
import com.aguardientes.azarcafetero.user_service.infrastructure.adapter.in.web.dto.UpdateUsernameRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Profile", description = "Gestión de perfil del usuario")  // ← aquí
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
    @Operation(summary = "Actualizar avatar")  // ← aquí
    @PutMapping("/avatar")
    public ResponseEntity<?> updateAvatar(
            @Parameter(description = "ID del usuario", required = true)  // ← aquí
            @RequestHeader("X-User-Id") String userId,
            @RequestBody UpdateAvatarRequest request) {

        updateAvatarUseCase.execute(userId, request.getAvatarUrl());
        return ResponseEntity.ok(Map.of("message", "Avatar actualizado correctamente"));
    }

    // PUT /profile/username
    @Operation(summary = "Actualizar username", description = "Cooldown de 30 días")  // ← aquí
    @PutMapping("/username")
    public ResponseEntity<?> updateUsername(
            @Parameter(description = "ID del usuario", required = true)  // ← aquí
            @RequestHeader("X-User-Id") String userId,
            @RequestBody UpdateUsernameRequest request) {

        UsernameUpdateResult result = updateUsernameUseCase.execute(userId, request.getUsername());

        if (result.isSuccess()) {
            return ResponseEntity.ok(Map.of("message", result.getMessage()));
        }

        if (result.getSuggestions() != null) {
            return ResponseEntity.status(409).body(Map.of(
                    "message", result.getMessage(),
                    "suggestions", result.getSuggestions()
            ));
        }

        return ResponseEntity.status(403).body(Map.of(
                "message", result.getMessage(),
                "daysUntilNextChange", result.getDaysUntilNextChange()
        ));
    }

    // GET /profile/status
    @Operation(summary = "Obtener estado del perfil")  // ← aquí
    @GetMapping("/status")
    public ResponseEntity<ProfileStatusResponse> getStatus(
            @Parameter(description = "ID del usuario", required = true)  // ← aquí
            @RequestHeader("X-User-Id") String userId) {

        ProfileStatusResponse status = getProfileStatusUseCase.execute(userId);
        return ResponseEntity.ok(status);
    }
}