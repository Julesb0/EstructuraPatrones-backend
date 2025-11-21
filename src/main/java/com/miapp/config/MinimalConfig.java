package com.miapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Configuración ultra-simple para evitar errores de Spring Boot
 */
@Configuration
@ComponentScan(basePackages = "com.miapp")
public class MinimalConfig {
    // Configuración vacía para evitar errores de parseo
}