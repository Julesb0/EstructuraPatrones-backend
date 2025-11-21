package com.miapp.auth.web;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:5173", "https://estructura-patrones-frontend-ljgk.vercel.app"}, allowCredentials = "true")
public class DebugController {

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of(
            "status", "pong",
            "service", "auth-service",
            "timestamp", System.currentTimeMillis()
        );
    }
}