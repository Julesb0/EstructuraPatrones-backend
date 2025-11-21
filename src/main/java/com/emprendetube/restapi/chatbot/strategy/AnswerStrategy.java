package com.emprendetube.restapi.chatbot.strategy;

import com.emprendetube.restapi.entity.ChatMessageCategory;

public interface AnswerStrategy {
    
    /**
     * Determina si esta estrategia puede manejar la categoría del mensaje
     */
    boolean canHandle(ChatMessageCategory category);
    
    /**
     * Genera una respuesta basada en el contenido del mensaje
     */
    String generateAnswer(String userMessage);
    
    /**
     * Obtiene el nombre de la estrategia
     */
    String getStrategyName();
}