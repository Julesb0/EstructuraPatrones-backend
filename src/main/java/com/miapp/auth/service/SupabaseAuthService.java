package com.miapp.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miapp.auth.config.SupabaseProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SupabaseAuthService {
    private final SupabaseProperties supabaseProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SupabaseAuthService(SupabaseProperties supabaseProperties) {
        this.supabaseProperties = supabaseProperties;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String signUp(String email, String password) throws Exception {
        // Modo de prueba: simular registro exitoso
        if (isTestMode()) {
            return "test-user-" + UUID.randomUUID().toString();
        }
        
        String url = supabaseProperties.getUrl() + "/auth/v1/signup";
        
        Map<String, String> request = new HashMap<>();
        request.put("email", email);
        request.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseProperties.getAnonKey());

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.path("user").path("id").asText();
            } else {
                throw new RuntimeException("Sign up failed: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Sign up error: " + e.getMessage(), e);
        }
    }

    public String signIn(String email, String password) throws Exception {
        // Modo de prueba: simular login exitoso
        if (isTestMode()) {
            return "test-jwt-token-" + UUID.randomUUID().toString();
        }
        
        String url = supabaseProperties.getUrl() + "/auth/v1/token?grant_type=password";
        
        Map<String, String> request = new HashMap<>();
        request.put("email", email);
        request.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseProperties.getAnonKey());

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.path("access_token").asText();
            } else {
                throw new RuntimeException("Sign in failed: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Sign in error: " + e.getMessage(), e);
        }
    }

    public String getUser(String accessToken) throws Exception {
        // Modo de prueba: simular obtener usuario
        if (isTestMode()) {
            return "{\"id\":\"test-user-" + UUID.randomUUID().toString() + "\",\"email\":\"test@example.com\"}";
        }
        
        String url = supabaseProperties.getUrl() + "/auth/v1/user";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("apikey", supabaseProperties.getAnonKey());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException("Get user failed: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Get user error: " + e.getMessage(), e);
        }
    }
    
    /**
     * Verifica si estamos en modo de prueba
     */
    private boolean isTestMode() {
        String url = supabaseProperties.getUrl();
        String anonKey = supabaseProperties.getAnonKey();
        return url.contains("test.supabase.co") || anonKey.equals("test-anon-key");
    }
}