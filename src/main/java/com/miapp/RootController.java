package com.miapp;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:5173", "https://estructura-patrones-frontend-ljgk.vercel.app"}, allowCredentials = "true")
public class RootController {

    @GetMapping("/")
    public Map<String, Object> root() {
        return Map.of(
            "message", "¡HotCash Backend está funcionando! 🚀",
            "version", "1.0.0",
            "timestamp", LocalDateTime.now().toString(),
            "endpoints", Map.of(
                "api", "/api",
                "health", "/api/debug/ping",
                "docs", "Consulta /api/debug/ping para verificar estado"
            ),
            "status", "running"
        );
    }

    // Health endpoint removido - ya existe en RailwayHealthController
}