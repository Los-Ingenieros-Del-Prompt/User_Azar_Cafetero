package com.aguardientes.azarcafetero.user_service.infrastructure.adapter.in.web;

import com.aguardientes.azarcafetero.user_service.application.dto.ProfileStatusResponse;
import com.aguardientes.azarcafetero.user_service.domain.model.UsernameUpdateResult;
import com.aguardientes.azarcafetero.user_service.domain.port.in.GetProfileStatusUseCase;
import com.aguardientes.azarcafetero.user_service.domain.port.in.UpdateAvatarUseCase;
import com.aguardientes.azarcafetero.user_service.domain.port.in.UpdateUsernameUseCase;
import com.aguardientes.azarcafetero.user_service.infrastructure.adapter.in.web.dto.UpdateAvatarRequest;
import com.aguardientes.azarcafetero.user_service.infrastructure.adapter.in.web.dto.UpdateUsernameRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Profile", description = "Gestión de perfil del usuario")
@CrossOrigin(origins = "http://localhost:3000")
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
    @Operation(summary = "Actualizar avatar")
    @PutMapping("/avatar")
    public ResponseEntity<?> updateAvatar(
            Authentication auth,
            @RequestBody UpdateAvatarRequest request) {

        String userId = auth.getName();

        updateAvatarUseCase.execute(userId, request.getAvatarUrl());

        return ResponseEntity.ok(
                Map.of("message", "Avatar actualizado correctamente")
        );
    }

    // PUT /profile/username
    @Operation(summary = "Actualizar username", description = "Cooldown de 30 días")
    @PutMapping("/username")
    public ResponseEntity<?> updateUsername(
            Authentication auth,
            @RequestBody UpdateUsernameRequest request) {

        String userId = auth.getName();

        UsernameUpdateResult result =
                updateUsernameUseCase.execute(userId, request.getUsername());

        if (result.isSuccess()) {
            return ResponseEntity.ok(
                    Map.of("message", result.getMessage())
            );
        }

        if (result.getSuggestions() != null) {
            return ResponseEntity.status(409).body(
                    Map.of(
                            "message", result.getMessage(),
                            "suggestions", result.getSuggestions()
                    )
            );
        }

        return ResponseEntity.status(403).body(
                Map.of(
                        "message", result.getMessage(),
                        "daysUntilNextChange", result.getDaysUntilNextChange()
                )
        );
    }

    // GET /profile/status
    @Operation(summary = "Obtener estado del perfil")
    @GetMapping("/status")
    public ResponseEntity<ProfileStatusResponse> getStatus(Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.ok(
                    new ProfileStatusResponse(
                            null,
                            null,
                            false,
                            0
                    )
            );
        }

        String userId = auth.getName();

        ProfileStatusResponse status =
                getProfileStatusUseCase.execute(userId);

        return ResponseEntity.ok(status);
    }
}