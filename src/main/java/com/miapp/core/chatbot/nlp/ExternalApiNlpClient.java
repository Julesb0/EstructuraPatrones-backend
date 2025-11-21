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
 */
@Component
public class ExternalApiNlpClient implements NlpClient {

    private final RestTemplate restTemplate;
    private final String openAiApiKey;
    private final String openAiApiUrl = "https://api.openai.com/v1/chat/completions";

    public ExternalApiNlpClient() {
        this.restTemplate = new RestTemplate();
        Dotenv dotenv = Dotenv.load();
        this.openAiApiKey = dotenv.get("OPENAI_API_KEY");
    }

    @Override
    public String ask(String prompt) throws NlpException {
        try {
            // Si no hay API key, usar respuesta dummy para desarrollo
            if (openAiApiKey == null || openAiApiKey.isEmpty()) {
                return generateDummyResponse(prompt);
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

    /**
     * Genera respuestas dummy para desarrollo cuando no hay API key configurada
     */
    private String generateDummyResponse(String prompt) {
        if (prompt.toLowerCase().contains("hola") || prompt.toLowerCase().contains("buenos")) {
            return "¡Hola! Soy tu asistente emprendedor. Estoy aquí para ayudarte con temas legales, financieros y de marketing. ¿En qué puedo asistirte hoy?";
        } else if (prompt.length() < 20) {
            return "Gracias por tu mensaje. Como asistente para emprendedores, puedo ayudarte con temas legales, financieros y de marketing. ¿Podrías darme más detalles sobre tu consulta?";
        } else {
            return "Entiendo tu consulta. Como asistente emprendedor, te sugiero que analices bien tu situación actual y consideres buscar asesoramiento especializado si es un tema complejo. ¿Hay algo más específico sobre lo que necesitas orientación?";
        }
    }
}