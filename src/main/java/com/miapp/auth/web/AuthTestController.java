package com.miapp.auth.web;

import com.miapp.auth.domain.AuthResponse;
import com.miapp.auth.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "https://estructurapatrones-frontend.vercel.app"}, allowCredentials = "true")
public class AuthTestController {
    
    @Autowired
    private JwtService jwtService;
    
    /**
     * Endpoint de prueba para generar un token JWT válido
     * Solo para desarrollo/testing
     */
    @GetMapping("/test-token")
    public AuthResponse generateTestToken() {
        // Generar un token JWT válido para pruebas
        String userId = "test-user-" + UUID.randomUUID().toString().substring(0, 8);
        String username = "testuser";
        
        String token = jwtService.generateToken(userId, username);
        
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUsername(username);
        
        return response;
    }
}