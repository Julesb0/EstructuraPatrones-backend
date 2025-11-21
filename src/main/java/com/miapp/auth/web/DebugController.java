package com.miapp.auth.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
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