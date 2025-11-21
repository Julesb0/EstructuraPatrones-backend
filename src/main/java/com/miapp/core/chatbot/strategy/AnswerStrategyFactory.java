package com.miapp.core.chatbot.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Factory para seleccionar la estrategia de respuesta adecuada
 * Implementa el patrón Factory
 */
@Component
public class AnswerStrategyFactory {
    
    private final List<AnswerStrategy> strategies;
    private final DefaultAnswerStrategy defaultStrategy;

    @Autowired
    public AnswerStrategyFactory(List<AnswerStrategy> strategies, DefaultAnswerStrategy defaultStrategy) {
        this.strategies = strategies;
        this.defaultStrategy = defaultStrategy;
    }

    /**
     * Selecciona la estrategia apropiada basándose en la categoría del mensaje
     */
    public AnswerStrategy getStrategy(String category) {
        if (category == null) {
            return defaultStrategy;
        }

        String upperCategory = category.toUpperCase();
        
        for (AnswerStrategy strategy : strategies) {
            if (!strategy.getStrategyName().equals("DefaultAnswerStrategy")) {
                if (strategy.getStrategyName().toUpperCase().contains(upperCategory)) {
                    return strategy;
                }
            }
        }
        
        return defaultStrategy;
    }

    /**
     * Selecciona la estrategia apropiada basándose en el contenido de la pregunta
     */
    public AnswerStrategy getStrategyForQuestion(String question) {
        if (question == null) {
            return defaultStrategy;
        }

        for (AnswerStrategy strategy : strategies) {
            if (!strategy.getStrategyName().equals("DefaultAnswerStrategy") && strategy.canHandle(question)) {
                return strategy;
            }
        }
        
        return defaultStrategy;
    }
}