package com.miapp.core.profile.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miapp.auth.config.SupabaseProperties;
import com.miapp.core.profile.domain.UserProfile;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SupabaseProfileRepository implements ProfileRepository {
    
    private final SupabaseProperties supabaseProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final String TABLE_NAME = "profiles";

    public SupabaseProfileRepository(SupabaseProperties supabaseProperties) {
        this.supabaseProperties = supabaseProperties;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Optional<UserProfile> findByUserId(UUID userId) {
        String url = supabaseProperties.getUrl() + "/rest/v1/" + TABLE_NAME + "?id=eq." + userId.toString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseProperties.getServiceRoleKey());
        headers.set("Authorization", "Bearer " + supabaseProperties.getServiceRoleKey());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode root = objectMapper.readTree(response.getBody());
                if (root.size() > 0) {
                    UserProfile profile = convertJsonNodeToProfile(root.get(0));
                    return Optional.of(profile);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding profile by userId: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public UserProfile save(UserProfile profile) {
        String url = supabaseProperties.getUrl() + "/rest/v1/" + TABLE_NAME;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseProperties.getServiceRoleKey());
        headers.set("Authorization", "Bearer " + supabaseProperties.getServiceRoleKey());
        headers.set("Prefer", "return=representation");

        // Convertir a formato JSON para Supabase
        String jsonData = convertProfileToJson(profile);
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonData, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode root = objectMapper.readTree(response.getBody());
                if (root.size() > 0) {
                    return convertJsonNodeToProfile(root.get(0));
                }
            } else {
                throw new RuntimeException("Save failed: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Save error: " + e.getMessage(), e);
        }
        return profile;
    }

    @Override
    public UserProfile update(UserProfile profile) {
        String url = supabaseProperties.getUrl() + "/rest/v1/" + TABLE_NAME + "?id=eq." + profile.getUserId().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseProperties.getServiceRoleKey());
        headers.set("Authorization", "Bearer " + supabaseProperties.getServiceRoleKey());
        headers.set("Prefer", "return=representation");

        // Solo actualizar los campos que pueden cambiar
        String jsonData = "{\"full_name\":\"" + profile.getFullName() + "\",\"role\":\"" + profile.getRole() + "\",\"country\":\"" + profile.getCountry() + "\",\"updated_at\":\"" + LocalDateTime.now() + "\"}";
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonData, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, httpEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode root = objectMapper.readTree(response.getBody());
                if (root.size() > 0) {
                    return convertJsonNodeToProfile(root.get(0));
                }
            } else {
                throw new RuntimeException("Update failed: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Update error: " + e.getMessage(), e);
        }
        return profile;
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return findByUserId(userId).isPresent();
    }

    private UserProfile convertJsonNodeToProfile(JsonNode node) {
        try {
            UserProfile profile = new UserProfile();
            
            if (node.has("id") && !node.get("id").isNull()) {
                profile.setUserId(UUID.fromString(node.get("id").asText()));
            }
            
            if (node.has("full_name") && !node.get("full_name").isNull()) {
                profile.setFullName(node.get("full_name").asText());
            }
            
            if (node.has("role") && !node.get("role").isNull()) {
                profile.setRole(node.get("role").asText());
            }
            
            if (node.has("country") && !node.get("country").isNull()) {
                profile.setCountry(node.get("country").asText());
            }
            
            if (node.has("created_at") && !node.get("created_at").isNull()) {
                profile.setCreatedAt(LocalDateTime.parse(node.get("created_at").asText().replace(" ", "T")));
            }
            
            if (node.has("updated_at") && !node.get("updated_at").isNull()) {
                profile.setUpdatedAt(LocalDateTime.parse(node.get("updated_at").asText().replace(" ", "T")));
            }
            
            return profile;
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to profile: " + e.getMessage(), e);
        }
    }

    private String convertProfileToJson(UserProfile profile) {
        return "{\"id\":\"" + profile.getUserId() + "\",\"full_name\":\"" + profile.getFullName() + "\",\"role\":\"" + profile.getRole() + "\",\"country\":\"" + profile.getCountry() + "\",\"created_at\":\"" + profile.getCreatedAt() + "\",\"updated_at\":\"" + profile.getUpdatedAt() + "\"}";
    }
}