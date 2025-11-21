package com.miapp.auth.facade;

import com.miapp.auth.datastruct.UserStoreSingleton;
import com.miapp.auth.domain.AuthResponse;
import com.miapp.auth.domain.User;
import com.miapp.auth.factory.AuthServiceFactory;
import com.miapp.auth.service.AuthService;
import com.miapp.auth.service.JwtService;
import com.miapp.auth.service.SupabaseTokenValidator;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class AuthFacade {
    private final AuthServiceFactory authServiceFactory;
    private final UserStoreSingleton userStore;
    private final SupabaseTokenValidator supabaseTokenValidator;
    private final JwtService jwtService;

    public AuthFacade(AuthServiceFactory authServiceFactory, SupabaseTokenValidator supabaseTokenValidator, JwtService jwtService) {
        this.authServiceFactory = authServiceFactory;
        this.userStore = UserStoreSingleton.getInstance();
        this.supabaseTokenValidator = supabaseTokenValidator;
        this.jwtService = jwtService;
    }

    public AuthResponse register(String email, String password, String username) throws Exception {
        AuthService authService = authServiceFactory.createAuthService();
        AuthResponse response = authService.register(email, password, username);
        
        User user = authService.getUserById(getUserIdFromToken(response.getToken()));
        if (user != null) {
            userStore.addUser(user);
        }
        
        return response;
    }

    public AuthResponse login(String email, String password) throws Exception {
        AuthService authService = authServiceFactory.createAuthService();
        AuthResponse response = authService.login(email, password);
        
        User user = authService.getUserById(getUserIdFromToken(response.getToken()));
        if (user != null) {
            userStore.addUser(user);
        }
        
        return response;
    }

    public User getCurrentUser(String token) {
        String userId = getUserIdFromToken(token);
        return userStore.getUser(userId);
    }

    /**
     * Procesa el login social
     * 
     * @param accessToken Token de acceso de Supabase
     * @param email Email del usuario
     * @param provider Proveedor OAuth (google, facebook, azure)
     * @return AuthResponse con el token JWT
     * @throws Exception si hay error en el proceso
     */
    public AuthResponse socialLogin(String accessToken, String email, String provider) throws Exception {
        // Validar el token de Supabase
        Map<String, Object> userData = supabaseTokenValidator.validateSupabaseToken(accessToken, provider);
        if (userData == null) {
            throw new Exception("Invalid or expired Supabase token");
        }
        
        // Verificar que el email coincida
        String tokenEmail = (String) userData.get("email");
        if (tokenEmail == null || !tokenEmail.equalsIgnoreCase(email)) {
            throw new Exception("Email mismatch between token and request");
        }
        
        // Generar username a partir del email
        String username = email.split("@")[0];
        if (username.length() < 3) {
            username = username + "_user";
        }
        
        // Generar userId único
        String userId = "social_" + email.hashCode();
        
        // Crear token JWT directamente para usuarios sociales
        String token = jwtService.generateToken(userId, username);
        
        // Crear respuesta exitosa
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUsername(username);
        
        return response;
    }

    private String getUserIdFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;
            
            String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(payload);
            return node.get("sub").asText();
        } catch (Exception e) {
            return null;
        }
    }
}