package com.miapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Aplicación ultra-simple para Render - BACK READY FOR RENDER
 * Esta versión arranca garantizado sin errores de configuración
 */
@SpringBootApplication
@RestController
public class SimpleApplication {
    
    public static void main(String[] args) {
        System.out.println("🚀 INICIANDO APLICACIÓN SIMPLE PARA RENDER...");
        try {
            SpringApplication.run(SimpleApplication.class, args);
            System.out.println("✅ APLICACIÓN INICIADA CORRECTAMENTE");
        } catch (Exception e) {
            System.err.println("❌ ERROR CRÍTICO: " + e.getMessage());
            e.printStackTrace();
            // Intentar arrancar con configuración mínima
            System.setProperty("spring.main.banner-mode", "off");
            System.setProperty("spring.main.lazy-initialization", "true");
            System.setProperty("spring.autoconfigure.exclude", "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration");
            SpringApplication.run(SimpleApplication.class, args);
        }
    }
    
    // Root endpoint removido - ya existe en RailwayHealthController
    
    // Health endpoint removido - ya existe en RailwayHealthController
}