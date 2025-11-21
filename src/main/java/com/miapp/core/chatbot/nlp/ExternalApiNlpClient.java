package com.miapp.core.chatbot.nlp;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementación concreta del patrón Adapter/Bridge
 * Adapta la API externa de OpenAI al contrato NlpClient
 * Nota: No usar @Component aquí, se configura manualmente en NlpClientConfig
 */
public class ExternalApiNlpClient implements NlpClient {

    private final RestTemplate restTemplate;
    private final String openAiApiKey;
    private final String openAiApiUrl = "https://api.openai.com/v1/chat/completions";

    public ExternalApiNlpClient() {
        this.restTemplate = new RestTemplate();
        // Intentar obtener la API key de las variables de entorno
        this.openAiApiKey = System.getenv("OPENAI_API_KEY");
    }

    @Override
    public String ask(String prompt) throws NlpException {
        try {
            // Si no hay API key, lanzar excepción para que el FallbackNlpClient maneje
            if (openAiApiKey == null || openAiApiKey.isEmpty()) {
                throw new NlpException("OPENAI_API_KEY no configurada");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + openAiApiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", new Object[]{
                    Map.of("role", "system", "content", "Eres un asistente para emprendedores. Responde de forma clara y concisa."),
                    Map.of("role", "user", "content", prompt)
            });
            requestBody.put("max_tokens", 150);
            requestBody.put("temperature", 0.7);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            Map<String, Object> response = restTemplate.postForObject(openAiApiUrl, request, Map.class);
            
            if (response != null && response.containsKey("choices")) {
                java.util.List<Map<String, Object>> choices = (java.util.List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
            
            return "Lo siento, no pude generar una respuesta adecuada.";
            
        } catch (Exception e) {
            throw new NlpException("Error al comunicarse con el servicio de NLP: " + e.getMessage(), e);
        }
    }

    @Override
    public String getClientInfo() {
        return "ExternalApiNlpClient (OpenAI GPT-3.5)";
    }


}