package com.aguardientes.azarcafetero.user_service.infrastructure.adapter.in.web.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Entrada inválida / error de validación → 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(400).body(Map.of(
                "error", ex.getMessage()
        ));
    }

    // Lógica de dominio violada → 400
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(400).body(Map.of(
                "error", ex.getMessage()
        ));
    }

    // Error genérico → 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        return ResponseEntity.status(500).body(Map.of(
                "error", "Error interno del servidor"
        ));
    }
}
