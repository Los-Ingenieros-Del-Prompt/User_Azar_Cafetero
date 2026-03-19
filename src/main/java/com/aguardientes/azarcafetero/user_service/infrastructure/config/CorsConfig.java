package com.aguardientes.azarcafetero.user_service.infrastructure.config;

import org.springframework.context.annotation.Configuration;

/**
 * CORS desactivado — lo maneja el gateway en puerto 8080.
 * Tenerlo aquí duplica el header y el browser lo rechaza.
 */
@Configuration
public class CorsConfig {
    // vacío
}