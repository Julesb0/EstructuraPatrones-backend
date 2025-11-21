package com.miapp.auth.web;

import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class SocialLoginTestController {

    @PostMapping("/social-login-mock")
    public Map<String, Object> mockSocialLogin(@RequestBody Map<String, String> request) {
        String accessToken = request.get("accessToken");
        String email = request.get("email");
        String provider = request.get("provider");
        
        if (accessToken == null || email == null || provider == null) {
            return Map.of(
                "success", false,
                "message", "Missing required fields"
            );
        }
        
        // Simular respuesta exitosa de login social
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("token", "mock-jwt-token-" + System.currentTimeMillis());
        response.put("username", email.split("@")[0]);
        response.put("email", email);
        response.put("provider", provider);
        response.put("message", "Social login successful (mock)");
        
        return response;
    }
    
    @GetMapping("/social-providers")
    public Map<String, Object> getSocialProviders() {
        return Map.of(
            "providers", new String[]{"google", "facebook", "azure"},
            "status", "available"
        );
    }
}