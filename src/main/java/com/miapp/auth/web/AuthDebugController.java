package com.miapp.auth.web;

import com.miapp.auth.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "https://estructurapatrones-frontend.vercel.app"}, allowCredentials = "true")
public class AuthDebugController {
    
    @Autowired
    private JwtService jwtService;
    
    @GetMapping("/debug/token")
    public Map<String, Object> debugToken(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Extraer el token del header
            String authorizationHeader = request.getHeader("Authorization");
            
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                
                result.put("tokenPresent", true);
                result.put("tokenLength", token.length());
                result.put("tokenPreview", token.substring(0, Math.min(20, token.length())) + "...");
                
                // Validar el token
                boolean isValid = jwtService.validateToken(token);
                result.put("isValid", isValid);
                
                if (isValid) {
                    String userId = jwtService.extractUserId(token);
                    String username = jwtService.extractUsername(token);
                    result.put("userId", userId);
                    result.put("username", username);
                    result.put("isExpired", jwtService.isTokenExpired(token));
                } else {
                    result.put("error", "Token inválido o expirado");
                }
                
            } else {
                result.put("tokenPresent", false);
                result.put("error", "No se encontró token Bearer en el header");
            }
            
        } catch (Exception e) {
            result.put("error", "Error procesando token: " + e.getMessage());
        }
        
        return result;
    }
}