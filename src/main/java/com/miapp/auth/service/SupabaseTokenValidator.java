package com.miapp.auth.service;

import com.miapp.auth.config.SupabaseProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.security.Key;
import java.util.Base64;
import java.util.Map;
import java.util.HashMap;

@Service
public class SupabaseTokenValidator {
    
    private static final Logger logger = LoggerFactory.getLogger(SupabaseTokenValidator.class);
    
    private final SupabaseProperties supabaseProperties;
    private final RestTemplate restTemplate;
    
    public SupabaseTokenValidator(SupabaseProperties supabaseProperties, RestTemplate restTemplate) {
        this.supabaseProperties = supabaseProperties;
        this.restTemplate = restTemplate;
    }
    
    /**
     * Valida un token de acceso de Supabase
     * 
     * @param accessToken El token JWT de Supabase
     * @param provider El proveedor OAuth (google, facebook, azure)
     * @return Map con la información del usuario si es válido, null si no es válido
     */
    public Map<String, Object> validateSupabaseToken(String accessToken, String provider) {
        try {
            logger.debug("Validando token de Supabase para provider: {}", provider);
            
            // Modo de prueba: si estamos usando credenciales de test, simular validación exitosa
            if (isTestMode()) {
                logger.info("Modo de prueba activado - simulando validación exitosa");
                return createMockUserData(accessToken, provider);
            }
            
            // Opción 1: Validar contra la API de Supabase (más seguro)
            return validateWithSupabaseAPI(accessToken);
            
        } catch (Exception e) {
            logger.error("Error validando token de Supabase: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Valida el token contra la API de Supabase
     */
    private Map<String, Object> validateWithSupabaseAPI(String accessToken) {
        try {
            String url = supabaseProperties.getUrl() + "/auth/v1/user";
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.set("apikey", supabaseProperties.getAnonKey());
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> userData = response.getBody();
                logger.debug("Token válido, usuario: {}", userData.get("email"));
                return userData;
            }
            
            return null;
            
        } catch (Exception e) {
            logger.error("Error validando con Supabase API: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Extrae el email del token JWT (método de respaldo)
     */
    public String extractEmailFromToken(String accessToken) {
        try {
            // Decodificar el payload del JWT sin verificar la firma
            String[] parts = accessToken.split("\\.");
            if (parts.length != 3) {
                return null;
            }
            
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            Map<String, Object> claims = new HashMap<>();
            // Parsear el JSON manualmente o usar ObjectMapper
            // Por simplicidad, asumimos que el email está en el payload
            
            // Buscar email en el payload
            if (payload.contains("\"email\":")) {
                int start = payload.indexOf("\"email\":") + 9;
                int end = payload.indexOf("\"", start);
                if (end > start) {
                    return payload.substring(start, end);
                }
            }
            
            return null;
            
        } catch (Exception e) {
            logger.error("Error extrayendo email del token: {}", e.getMessage());
            return null;
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
    
    /**
     * Crea datos de usuario mock para modo de prueba
     */
    private Map<String, Object> createMockUserData(String accessToken, String provider) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", "test-user-" + System.currentTimeMillis());
        userData.put("email", "testuser@" + provider + ".com");
        userData.put("app_metadata", Map.of("provider", provider));
        userData.put("user_metadata", Map.of("name", "Test User"));
        userData.put("created_at", new java.util.Date().toString());
        return userData;
    }
    
    /**
     * Verifica si el token está expirado
     */
    public boolean isTokenExpired(String accessToken) {
        try {
            String[] parts = accessToken.split("\\.");
            if (parts.length != 3) {
                return true;
            }
            
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            
            // Buscar expiración (exp) en el payload
            if (payload.contains("\"exp\":")) {
                int start = payload.indexOf("\"exp\":") + 6;
                int end = payload.indexOf(",", start);
                if (end == -1) {
                    end = payload.indexOf("}", start);
                }
                
                if (end > start) {
                    long exp = Long.parseLong(payload.substring(start, end).trim());
                    long currentTime = System.currentTimeMillis() / 1000;
                    return currentTime > exp;
                }
            }
            
            return true; // Si no puede parsear, asumir expirado
            
        } catch (Exception e) {
            logger.error("Error verificando expiración del token: {}", e.getMessage());
            return true;
        }
    }
}