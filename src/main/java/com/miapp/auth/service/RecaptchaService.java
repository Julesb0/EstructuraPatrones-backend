package com.miapp.auth.service;

import com.miapp.auth.config.RecaptchaProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.HashMap;

@Service
public class RecaptchaService {
    
    private static final Logger logger = LoggerFactory.getLogger(RecaptchaService.class);
    
    private final RecaptchaProperties recaptchaProperties;
    private final RestTemplate restTemplate;
    
    public RecaptchaService(RecaptchaProperties recaptchaProperties, RestTemplate restTemplate) {
        this.recaptchaProperties = recaptchaProperties;
        this.restTemplate = restTemplate;
    }
    
    /**
     * Valida un token de reCAPTCHA con la API de Google
     * 
     * @param recaptchaToken El token a validar
     * @param clientIp La IP del cliente (opcional, puede ser null)
     * @return true si el token es válido, false en caso contrario
     */
    public boolean isValid(String recaptchaToken, String clientIp) {
        // Si no hay clave secreta configurada, permitir el acceso (modo desarrollo)
        if (recaptchaProperties.getSecretKey() == null || recaptchaProperties.getSecretKey().trim().isEmpty()) {
            logger.warn("reCAPTCHA secret key not configured - allowing request");
            return true;
        }
        
        // Si no hay token, rechazar
        if (recaptchaToken == null || recaptchaToken.trim().isEmpty()) {
            logger.warn("reCAPTCHA token is missing");
            return false;
        }
        
        try {
            // Preparar los parámetros de la petición
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("secret", recaptchaProperties.getSecretKey());
            params.add("response", recaptchaToken);
            if (clientIp != null && !clientIp.trim().isEmpty()) {
                params.add("remoteip", clientIp);
            }
            
            logger.debug("Validating reCAPTCHA token with Google API");
            
            // Hacer la petición a la API de Google
            ResponseEntity<Map> response = restTemplate.postForEntity(
                recaptchaProperties.getVerifyUrl(),
                params,
                Map.class
            );
            
            if (response.getBody() != null) {
                Boolean success = (Boolean) response.getBody().get("success");
                Double score = response.getBody().get("score") != null ? 
                    Double.parseDouble(response.getBody().get("score").toString()) : null;
                
                logger.debug("reCAPTCHA validation result: success={}, score={}", success, score);
                
                // Para reCAPTCHA v2, solo necesitamos verificar el éxito
                // Para reCAPTCHA v3, podríamos también verificar el score (0.0 - 1.0)
                return Boolean.TRUE.equals(success);
            }
            
            logger.error("Empty response from reCAPTCHA API");
            return false;
            
        } catch (ResourceAccessException e) {
            // Error de red (timeout, conexión fallida, etc.)
            logger.error("Network error while validating reCAPTCHA: {}", e.getMessage());
            
            // Según la configuración, podemos:
            // - failOnError = true: rechazar la petición (más seguro)
            // - failOnError = false: permitir la petición (menos seguro)
            return !recaptchaProperties.isFailOnError();
            
        } catch (Exception e) {
            // Cualquier otro error
            logger.error("Unexpected error while validating reCAPTCHA: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Versión simplificada sin IP del cliente
     */
    public boolean isValid(String recaptchaToken) {
        return isValid(recaptchaToken, null);
    }
}