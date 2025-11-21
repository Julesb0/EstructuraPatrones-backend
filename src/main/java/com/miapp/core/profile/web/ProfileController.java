package com.miapp.core.profile.web;

import com.miapp.auth.service.JwtService;
import com.miapp.core.profile.facade.ProfileFacade;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3002", "https://estructurapatrones-frontend.vercel.app"}, allowCredentials = "true")
public class ProfileController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    
    private final ProfileFacade profileFacade;
    private final JwtService jwtService;

    @Autowired
    public ProfileController(ProfileFacade profileFacade, JwtService jwtService) {
        this.profileFacade = profileFacade;
        this.jwtService = jwtService;
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile(HttpServletRequest request) {
        try {
            // Extraer userId del JWT (asumiendo que ya está implementado)
            String userId = extractUserIdFromRequest(request);
            
            if (userId == null || userId.isEmpty()) {
                logger.error("No se pudo extraer el userId del token JWT");
                return ResponseEntity.status(401).build();
            }

            Optional<ProfileResponse> profile = profileFacade.getCurrentUserProfile(UUID.fromString(userId));
            
            if (profile.isPresent()) {
                return ResponseEntity.ok(profile.get());
            } else {
                // Si no existe el perfil, devolver un perfil vacío para que el frontend lo maneje
                logger.info("No se encontró perfil para el usuario: {}", userId);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error obteniendo perfil: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/me")
    public ResponseEntity<ProfileResponse> updateMyProfile(
            HttpServletRequest request,
            @Valid @RequestBody ProfileUpdateRequest updateRequest) {
        try {
            // Extraer userId del JWT
            String userId = extractUserIdFromRequest(request);
            
            if (userId == null || userId.isEmpty()) {
                logger.error("No se pudo extraer el userId del token JWT");
                return ResponseEntity.status(401).build();
            }

            ProfileResponse updatedProfile = profileFacade.updateProfile(UUID.fromString(userId), updateRequest);
            return ResponseEntity.ok(updatedProfile);
            
        } catch (IllegalArgumentException e) {
            logger.error("Error de validación actualizando perfil: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error actualizando perfil: {}", e.getMessage(), e);
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