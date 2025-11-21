package com.miapp.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miapp.auth.config.JwtProperties;
import com.miapp.auth.domain.AuthResponse;
import com.miapp.auth.domain.User;
import org.springframework.stereotype.Service;

@Service
public class SupabaseAuthAdapter implements AuthService {
    private final SupabaseAuthService supabaseAuthService;
    private final SupabaseAdminService supabaseAdminService;
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;

    public SupabaseAuthAdapter(SupabaseAuthService supabaseAuthService, 
                              SupabaseAdminService supabaseAdminService,
                              JwtProperties jwtProperties) {
        this.supabaseAuthService = supabaseAuthService;
        this.supabaseAdminService = supabaseAdminService;
        this.jwtProperties = jwtProperties;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public AuthResponse register(String email, String password, String username) throws Exception {
        String userId = supabaseAuthService.signUp(email, password);
        supabaseAdminService.createUserProfile(userId, username, "ENTREPRENEUR");
        
        String token = generateJwtToken(userId, username);
        return new AuthResponse(token, username);
    }

    @Override
    public AuthResponse login(String email, String password) throws Exception {
        String accessToken = supabaseAuthService.signIn(email, password);
        String userInfo = supabaseAuthService.getUser(accessToken);
        
        JsonNode userNode = objectMapper.readTree(userInfo);
        String userId = userNode.path("id").asText();
        String emailFromUser = userNode.path("email").asText();
        
        JsonNode profile = supabaseAdminService.getUserProfile(userId);
        String username = profile != null ? profile.path("full_name").asText() : emailFromUser;
        
        String token = generateJwtToken(userId, username);
        return new AuthResponse(token, username);
    }

    @Override
    public User getUserById(String userId) throws Exception {
        JsonNode profile = supabaseAdminService.getUserProfile(userId);
        if (profile != null) {
            User user = new User();
            user.setId(userId);
            user.setUsername(profile.path("full_name").asText());
            user.setRole(profile.path("role").asText());
            return user;
        }
        return null;
    }

    private String generateJwtToken(String userId, String username) {
        long now = System.currentTimeMillis();
        long exp = now + jwtProperties.getExpMinutes() * 60 * 1000;
        
        return io.jsonwebtoken.Jwts.builder()
                .setSubject(userId)
                .claim("username", username)
                .setIssuedAt(new java.util.Date(now))
                .setExpiration(new java.util.Date(exp))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }
}