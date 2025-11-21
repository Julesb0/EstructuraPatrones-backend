package com.miapp.core.web;

import com.miapp.auth.service.JwtService;
import com.miapp.core.service.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3002"})
public class AnalyticsController {
    
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsController.class);
    
    private final AnalyticsService analyticsService;
    private final JwtService jwtService;

    public AnalyticsController(AnalyticsService analyticsService, JwtService jwtService) {
        this.analyticsService = analyticsService;
        this.jwtService = jwtService;
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getAnalyticsSummary(HttpServletRequest request) {
        try {
            // Extraer userId del JWT
            String userId = extractUserIdFromRequest(request);
            
            if (userId == null || userId.isEmpty()) {
                logger.error("No se pudo extraer el userId del token JWT");
                return ResponseEntity.status(401).build();
            }

            Map<String, Object> analytics = analyticsService.calculateUserAnalytics(userId);
            return ResponseEntity.ok(analytics);
            
        } catch (Exception e) {
            logger.error("Error obteniendo análisis: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    private String extractUserIdFromRequest(HttpServletRequest request) {
        // Extraer el token JWT del header Authorization
        String authorizationHeader = request.getHeader("Authorization");
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            
            try {
                // Validar el token y extraer el userId usando JwtService
                if (jwtService.validateToken(token)) {
                    return jwtService.extractUserId(token);
                } else {
                    logger.error("Token JWT inválido o expirado");
                }
            } catch (Exception e) {
                logger.error("Error extrayendo userId del token: {}", e.getMessage());
            }
        }
        
        return null;
    }
}