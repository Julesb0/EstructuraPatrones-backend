package com.miapp;

import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/mock")
@CrossOrigin(origins = "*")
public class MockSocialLoginController {

    @PostMapping("/social-login")
    public Map<String, Object> mockSocialLogin(@RequestBody Map<String, String> request) {
        String accessToken = request.get("accessToken");
        String email = request.get("email");
        String provider = request.get("provider");
        
        if (accessToken == null || email == null || provider == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Missing required fields: accessToken, email, provider");
            return error;
        }
        
        // Simular respuesta exitosa de login social
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("token", "mock-jwt-token-" + System.currentTimeMillis());
        response.put("username", email.split("@")[0]);
        response.put("email", email);
        response.put("provider", provider);
        response.put("message", "Social login successful (mock mode)");
        
        return response;
    }
    
    @GetMapping("/providers")
    public Map<String, Object> getProviders() {
        return Map.of(
            "providers", new String[]{"google", "facebook", "azure"},
            "status", "available",
            "mode", "mock"
        );
    }
}