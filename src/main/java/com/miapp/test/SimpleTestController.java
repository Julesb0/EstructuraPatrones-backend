package com.miapp.test;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/simple")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3002"})
public class SimpleTestController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Backend is running");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @PostMapping("/echo")
    public Map<String, Object> echo(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("echo", request);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @PostMapping("/mock-social-login")
    public Map<String, Object> mockSocialLogin(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        String email = (String) request.get("email");
        String provider = (String) request.get("provider");
        
        // Mock JWT token
        String mockToken = "mock_jwt_token_" + System.currentTimeMillis();
        
        response.put("success", true);
        response.put("message", "Mock social login successful");
        response.put("token", mockToken);
        response.put("user", Map.of(
            "id", "mock_user_" + (email != null ? email.hashCode() : "123"),
            "email", email != null ? email : "test@example.com",
            "username", email != null ? email.split("@")[0] : "testuser",
            "provider", provider != null ? provider : "google"
        ));
        
        return response;
    }
}