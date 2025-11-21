package com.miapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

/**
 * Aplicación alternativa para Render - BACK READY FOR RENDER
 * Esta versión tiene configuración mínima para evitar errores
 */
@SpringBootApplication
@Profile("render")
public class RenderApplication {
    
    public static void main(String[] args) {
        try {
            System.out.println("🚀 Iniciando aplicación para Render...");
            SpringApplication app = new SpringApplication(RenderApplication.class);
            
            // Configuración mínima para Render
            app.setAdditionalProfiles("render");
            System.setProperty("spring.main.banner-mode", "off");
            System.setProperty("spring.main.lazy-initialization", "true");
            
            app.run(args);
            System.out.println("✅ Aplicación iniciada correctamente");
        } catch (Exception e) {
            System.err.println("❌ Error iniciando aplicación: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}