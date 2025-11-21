package com.miapp.core.chatbot.nlp;

import org.springframework.stereotype.Component;

/**
 * Implementación alternativa del patrón Adapter/Bridge
 * Usa respuestas predefinidas cuando no hay API key configurada
 * Implementa el patrón Null Object
 */
@Component("fallbackNlpClient")
public class FallbackNlpClient implements NlpClient {

    @Override
    public String ask(String prompt) throws NlpException {
        return generateResponse(prompt);
    }

    @Override
    public String getClientInfo() {
        return "FallbackNlpClient (Respuestas Predefinidas)";
    }
    
    /**
     * Genera respuestas predefinidas basadas en el prompt
     */
    private String generateResponse(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            return "No entendí tu mensaje. ¿Podrías reformular tu pregunta?";
        }
        
        String lowerPrompt = prompt.toLowerCase().trim();
        
        // Respuestas de bienvenida
        if (lowerPrompt.contains("hola") || lowerPrompt.contains("buenos") || lowerPrompt.contains("hey")) {
            return "¡Hola! Soy tu asistente emprendedor virtual. Estoy aquí para ayudarte con temas legales, financieros y de marketing. ¿En qué puedo asistirte hoy?";
        }
        
        // Respuestas de despedida
        if (lowerPrompt.contains("adiós") || lowerPrompt.contains("hasta luego") || lowerPrompt.contains("gracias")) {
            return "¡Gracias por usar nuestro asistente! Si tienes más preguntas sobre tu emprendimiento, no dudes en volver. ¡Éxitos en tu proyecto!";
        }
        
        // Preguntas sobre cómo funciona
        if (lowerPrompt.contains("cómo") && lowerPrompt.contains("funciona")) {
            return "Soy un asistente especializado en temas de emprendimiento. Puedo ayudarte con consultas legales, financieras y de marketing. Simplemente escribe tu pregunta y te daré orientación básica. Para temas complejos, te recomiendo consultar con un profesional.";
        }
        
        // Preguntas muy cortas
        if (lowerPrompt.length() < 10) {
            return "Gracias por tu mensaje. Como asistente para emprendedores, puedo ayudarte con temas legales, financieros y de marketing. ¿Podrías darme más detalles sobre tu consulta?";
        }
        
        // Respuesta genérica para consultas más largas
        return "Entiendo tu consulta sobre emprendimiento. Como asistente virtual, te sugiero que analices bien tu situación actual y consideres buscar asesoramiento especializado si es un tema complejo. ¿Hay algo más específico sobre lo que necesitas orientación en temas legales, financieros o de marketing?";
    }
}