package com.miapp.core.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miapp.auth.config.SupabaseProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class SupabaseRepository {
    private final SupabaseProperties supabaseProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SupabaseRepository(SupabaseProperties supabaseProperties) {
        this.supabaseProperties = supabaseProperties;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public <T> List<T> findAllByUserId(String tableName, String userId, String userColumn, Class<T> clazz) throws Exception {
        String url = supabaseProperties.getUrl() + "/rest/v1/" + tableName + "?" + userColumn + "=eq." + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseProperties.getServiceRoleKey());
        headers.set("Authorization", "Bearer " + supabaseProperties.getServiceRoleKey());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode root = objectMapper.readTree(response.getBody());
                List<T> result = new ArrayList<>();
                
                for (JsonNode node : root) {
                    T item = convertJsonNodeToObject(node, clazz);
                    if (item != null) {
                        result.add(item);
                    }
                }
                return result;
            } else {
                throw new RuntimeException("Find failed: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Find error: " + e.getMessage(), e);
        }
    }

    public <T> T save(String tableName, T entity, Class<T> clazz) throws Exception {
        String url = supabaseProperties.getUrl() + "/rest/v1/" + tableName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseProperties.getServiceRoleKey());
        headers.set("Authorization", "Bearer " + supabaseProperties.getServiceRoleKey());
        headers.set("Prefer", "return=representation");

        String jsonData = objectMapper.writeValueAsString(entity);
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonData, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode root = objectMapper.readTree(response.getBody());
                if (root.size() > 0) {
                    return convertJsonNodeToObject(root.get(0), clazz);
                }
            } else {
                throw new RuntimeException("Save failed: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Save error: " + e.getMessage(), e);
        }
        return null;
    }

    private <T> T convertJsonNodeToObject(JsonNode node, Class<T> clazz) {
        try {
            Map<String, Object> result = new HashMap<>();
            
            node.fields().forEachRemaining(field -> {
                String key = field.getKey();
                JsonNode value = field.getValue();
                
                if (key.equals("created_at") || key.equals("updated_at")) {
                    if (!value.isNull()) {
                        result.put(key, LocalDateTime.parse(value.asText().replace(" ", "T")));
                    }
                } else if (key.equals("id") || key.equals("user_id")) {
                    result.put(key, value.asText());
                } else if (key.equals("title") || key.equals("summary") || key.equals("full_name") || key.equals("role") || key.equals("country")) {
                    result.put(key, value.asText());
                } else {
                    result.put(key, value.asText());
                }
            });
            
            return objectMapper.convertValue(result, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}