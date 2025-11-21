package com.emprendetube.restapi.chatbot.nlp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementación del patrón Decorator
 * Añade funcionalidad de logging al NlpClient sin modificar su implementación original
 */
@Component
public class LoggingNlpClient implements NlpClient {

    private static final Logger logger = LoggerFactory.getLogger(LoggingNlpClient.class);
    
    private final NlpClient wrappedClient;

    public LoggingNlpClient(NlpClient wrappedClient) {
        this.wrappedClient = wrappedClient;
    }

    @Override
    public String ask(String prompt) throws NlpException {
        logger.info("[NLP REQUEST] Prompt: {}", truncateForLogging(prompt));
        
        long startTime = System.currentTimeMillis();
        
        try {
            String response = wrappedClient.ask(prompt);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            logger.info("[NLP RESPONSE] Duration: {}ms, Response: {}", duration, truncateForLogging(response));
            
            return response;
            
        } catch (NlpException e) {
            logger.error("[NLP ERROR] Error processing prompt: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("[NLP ERROR] Unexpected error: {}", e.getMessage(), e);
            throw new NlpException("Unexpected error in NLP processing", e);
        }
    }

    @Override
    public String getClientInfo() {
        return "LoggingNlpClient[wrapped: " + wrappedClient.getClientInfo() + "]";
    }

    /**
     * Trunca el texto para logging para evitar logs demasiado largos
     */
    private String truncateForLogging(String text) {
        if (text == null) {
            return "null";
        }
        if (text.length() <= 100) {
            return text;
        }
        return text.substring(0, 97) + "...";
    }
}