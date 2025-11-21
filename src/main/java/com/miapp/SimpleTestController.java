package com.miapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class SimpleTestController {

    @GetMapping("/test")
    public Map<String, Object> test() {
        return Map.of(
            "status", "ok",
            "message", "Spring Boot is working!",
            "timestamp", System.currentTimeMillis()
        );
    }
}