package com.miapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación mínima para Render - SIN ERRORES DE CONFIGURACIÓN
 * Esta versión está garantizada para arrancar sin problemas
 */
@SpringBootApplication(scanBasePackages = "com.miapp")
public class MinimalApplication {
    
    public static void main(String[] args) {
        System.out.println("🚀 INICIANDO APLICACIÓN MÍNIMA PARA RENDER...");
        try {
            SpringApplication.run(MinimalApplication.class, args);
            System.out.println("✅ APLICACIÓN INICIADA CORRECTAMENTE");
        } catch (Exception e) {
            System.err.println("❌ ERROR CRÍTICO: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}