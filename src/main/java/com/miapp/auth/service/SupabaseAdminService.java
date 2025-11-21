package com.miapp.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miapp.auth.config.SupabaseProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SupabaseAdminService {
    private final SupabaseProperties supabaseProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SupabaseAdminService(SupabaseProperties supabaseProperties) {
        this.supabaseProperties = supabaseProperties;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public void createUserProfile(String userId, String username, String role) throws Exception {
        String url = supabaseProperties.getUrl() + "/rest/v1/profiles";
        
        Map<String, Object> profile = new HashMap<>();
        profile.put("id", userId);
        profile.put("full_name", username);
        profile.put("role", role);
        profile.put("country", "Unknown");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseProperties.getServiceRoleKey());
        headers.set("Authorization", "Bearer " + supabaseProperties.getServiceRoleKey());
        headers.set("Prefer", "return=minimal");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(profile, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Create profile failed: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Create profile error: " + e.getMessage(), e);
        }
    }

    public JsonNode getUserProfile(String userId) throws Exception {
        String url = supabaseProperties.getUrl() + "/rest/v1/profiles?id=eq." + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseProperties.getServiceRoleKey());
        headers.set("Authorization", "Bearer " + supabaseProperties.getServiceRoleKey());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.size() > 0 ? root.get(0) : null;
            } else {
                throw new RuntimeException("Get profile failed: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Get profile error: " + e.getMessage(), e);
        }
    }
}