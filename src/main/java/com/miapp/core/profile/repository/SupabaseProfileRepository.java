package com.miapp.core.profile.repository;

import com.miapp.core.profile.domain.UserProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Supabase implementation of ProfileRepository using REST API
 */
@Repository
public class SupabaseProfileRepository implements ProfileRepository {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service-role-key}")
    private String supabaseServiceKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Optional<UserProfile> findByUserId(String userId) {
        try {
            String url = supabaseUrl + "/rest/v1/profiles?id=eq." + userId;
            
            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map[]> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Map[].class);
            
            if (response.getBody() != null && response.getBody().length > 0) {
                Map<String, Object> data = response.getBody()[0];
                return Optional.of(mapToUserProfile(data));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching profile: " + e.getMessage(), e);
        }
    }

    @Override
    public UserProfile save(UserProfile profile) {
        try {
            if (existsByUserId(profile.getUserId())) {
                return updateProfile(profile);
            } else {
                return createProfile(profile);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error saving profile: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByUserId(String userId) {
        return findByUserId(userId).isPresent();
    }

    @Override
    public UserProfile createProfile(String userId, String fullName, String role, String country) {
        UserProfile profile = new UserProfile(userId, fullName, role, country);
        return createProfile(profile);
    }

    private UserProfile createProfile(UserProfile profile) {
        String url = supabaseUrl + "/rest/v1/profiles";
        
        HttpHeaders headers = createHeaders();
        headers.set("Prefer", "return=representation");
        
        Map<String, Object> data = new HashMap<>();
        data.put("id", profile.getUserId());
        data.put("full_name", profile.getFullName());
        data.put("role", profile.getRole());
        data.put("country", profile.getCountry());
        data.put("created_at", profile.getCreatedAt() != null ? profile.getCreatedAt().toString() : LocalDateTime.now().toString());
        data.put("updated_at", LocalDateTime.now().toString());
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(data, headers);
        ResponseEntity<Map[]> response = restTemplate.exchange(
            url, HttpMethod.POST, entity, Map[].class);
        
        if (response.getBody() != null && response.getBody().length > 0) {
            return mapToUserProfile(response.getBody()[0]);
        }
        throw new RuntimeException("Failed to create profile");
    }

    private UserProfile updateProfile(UserProfile profile) {
        String url = supabaseUrl + "/rest/v1/profiles?id=eq." + profile.getUserId();
        
        HttpHeaders headers = createHeaders();
        headers.set("Prefer", "return=representation");
        
        Map<String, Object> data = new HashMap<>();
        if (profile.getFullName() != null) data.put("full_name", profile.getFullName());
        if (profile.getRole() != null) data.put("role", profile.getRole());
        if (profile.getCountry() != null) data.put("country", profile.getCountry());
        data.put("updated_at", LocalDateTime.now().toString());
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(data, headers);
        ResponseEntity<Map[]> response = restTemplate.exchange(
            url, HttpMethod.PATCH, entity, Map[].class);
        
        if (response.getBody() != null && response.getBody().length > 0) {
            return mapToUserProfile(response.getBody()[0]);
        }
        throw new RuntimeException("Failed to update profile");
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseServiceKey);
        headers.set("Authorization", "Bearer " + supabaseServiceKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private UserProfile mapToUserProfile(Map<String, Object> data) {
        UserProfile profile = new UserProfile();
        profile.setUserId((String) data.get("id"));
        profile.setFullName((String) data.get("full_name"));
        profile.setRole((String) data.get("role"));
        profile.setCountry((String) data.get("country"));
        
        if (data.get("created_at") != null) {
            profile.setCreatedAt(LocalDateTime.parse((String) data.get("created_at")));
        }
        if (data.get("updated_at") != null) {
            profile.setUpdatedAt(LocalDateTime.parse((String) data.get("updated_at")));
        }
        
        return profile;
    }
}