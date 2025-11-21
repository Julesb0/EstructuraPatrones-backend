package com.miapp.core.chatbot.nlp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementación del patrón Decorator
 * Agrega logging a cualquier implementación de NlpClient
 */
public class LoggingNlpClient implements NlpClient {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingNlpClient.class);
    private final NlpClient delegate;

    public LoggingNlpClient(NlpClient delegate) {
        this.delegate = delegate;
    }

    @Override
    public String ask(String prompt) throws NlpException {
        logger.debug("Enviando prompt al servicio NLP: {}", prompt);
        long startTime = System.currentTimeMillis();
        
        try {
            String response = delegate.ask(prompt);
            long duration = System.currentTimeMillis() - startTime;
            
            logger.debug("Respuesta recibida del servicio NLP en {}ms: {}", duration, response);
            return response;
        } catch (NlpException e) {
            logger.error("Error en el servicio NLP: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public String getClientInfo() {
        return delegate.getClientInfo() + " [with logging]";
    }
}