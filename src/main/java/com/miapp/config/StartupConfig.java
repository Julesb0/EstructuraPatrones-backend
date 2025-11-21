package com.miapp.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class StartupConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(StartupConfig.class);
    
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("========================================");
        logger.info("🚀 APLICACIÓN INICIADA CORRECTAMENTE");
        logger.info("📋 Perfil activo: {}", System.getProperty("spring.profiles.active", "default"));
        logger.info("🔌 Puerto: {}", System.getenv("PORT") != null ? System.getenv("PORT") : "8080");
        logger.info("🌐 Supabase URL: {}", System.getenv("SUPABASE_URL") != null ? "Configurado" : "No configurado");
        logger.info("========================================");
        
        // Verificar variables críticas
        if (System.getenv("SUPABASE_SERVICE_ROLE_KEY") == null) {
            logger.warn("⚠️  ADVERTENCIA: SUPABASE_SERVICE_ROLE_KEY no está configurada");
        }
        
        if (System.getenv("JWT_SECRET") == null) {
            logger.warn("⚠️  ADVERTENCIA: JWT_SECRET no está configurada");
        }
    }
}