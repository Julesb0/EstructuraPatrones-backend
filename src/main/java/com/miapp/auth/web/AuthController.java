package com.miapp.auth.web;

import com.miapp.auth.domain.AuthResponse;
import com.miapp.auth.facade.AuthFacade;
import com.miapp.auth.service.RecaptchaService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final AuthFacade authFacade;
    private final RecaptchaService recaptchaService;

    public AuthController(AuthFacade authFacade, RecaptchaService recaptchaService) {
        this.authFacade = authFacade;
        this.recaptchaService = recaptchaService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request, HttpServletRequest httpRequest) {
        try {
            // Validar reCAPTCHA
            String clientIp = getClientIp(httpRequest);
            boolean isRecaptchaValid = recaptchaService.isValid(request.getRecaptchaToken(), clientIp);
            
            if (!isRecaptchaValid) {
                logger.warn("Invalid reCAPTCHA token from IP: {}", clientIp);
                AuthResponse errorResponse = new AuthResponse();
                errorResponse.setToken("");
                errorResponse.setUsername("reCAPTCHA inválido");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // Si reCAPTCHA es válido, continuar con el registro normal
            AuthResponse response = authFacade.register(request.getEmail(), request.getPassword(), request.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during registration: {}", e.getMessage(), e);
            AuthResponse errorResponse = new AuthResponse();
            errorResponse.setToken("");
            errorResponse.setUsername("Error en el registro");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            // Validar reCAPTCHA
            String clientIp = getClientIp(httpRequest);
            boolean isRecaptchaValid = recaptchaService.isValid(request.getRecaptchaToken(), clientIp);
            
            if (!isRecaptchaValid) {
                logger.warn("Invalid reCAPTCHA token from IP: {}", clientIp);
                AuthResponse errorResponse = new AuthResponse();
                errorResponse.setToken("");
                errorResponse.setUsername("reCAPTCHA inválido");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // Si reCAPTCHA es válido, continuar con el login normal
            AuthResponse response = authFacade.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during login: {}", e.getMessage(), e);
            AuthResponse errorResponse = new AuthResponse();
            errorResponse.setToken("");
            errorResponse.setUsername("Error en el login");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PostMapping("/social-login")
    public ResponseEntity<AuthResponse> socialLogin(@RequestBody SocialLoginRequest request) {
        try {
            logger.info("Social login attempt with provider: {} for email: {}", request.getProvider(), request.getEmail());
            
            AuthResponse response = authFacade.socialLogin(
                request.getAccessToken(), 
                request.getEmail(), 
                request.getProvider()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during social login: {}", e.getMessage(), e);
            AuthResponse errorResponse = new AuthResponse();
            errorResponse.setToken("");
            errorResponse.setUsername("Error en el login social: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Obtiene la IP del cliente del request HTTP
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}