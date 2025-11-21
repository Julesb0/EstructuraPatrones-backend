package com.miapp.core.chatbot.strategy;

import org.springframework.stereotype.Component;

/**
 * Implementación de estrategia para temas legales
 */
@Component
public class LegalAnswerStrategy implements AnswerStrategy {
    
    @Override
    public String generateAnswer(String question) {
        if (question.toLowerCase().contains("registro") || question.toLowerCase().contains("empresa")) {
            return "Para registrar tu empresa, necesitas considerar el tipo de sociedad más adecuada (SRL, SA, etc.). Te recomiendo consultar con un abogado especializado en derecho corporativo para elegir la mejor estructura legal para tu negocio.";
        } else if (question.toLowerCase().contains("contrato") || question.toLowerCase().contains("socio")) {
            return "Los contratos entre socios son fundamentales para evitar conflictos futuros. Asegúrate de definir claramente las responsabilidades, aportes y procedimientos de toma de decisiones.";
        } else if (question.toLowerCase().contains("impuesto") || question.toLowerCase().contains("iva")) {
            return "Los aspectos fiscales son cruciales para cualquier empresa. Te sugiero mantener tus libros contables al día y considerar contratar un contador profesional para asegurarte de cumplir con todas las obligaciones fiscales.";
        } else {
            return "Para temas legales específicos, te recomiendo consultar con un abogado especializado. Puedo ayudarte con información general, pero cada caso requiere análisis particular.";
        }
    }
    
    @Override
    public boolean canHandle(String question) {
        String lowerQuestion = question.toLowerCase();
        return lowerQuestion.contains("legal") || lowerQuestion.contains("ley") || 
               lowerQuestion.contains("registro") || lowerQuestion.contains("empresa") ||
               lowerQuestion.contains("contrato") || lowerQuestion.contains("socio") ||
               lowerQuestion.contains("impuesto") || lowerQuestion.contains("iva");
    }
    
    @Override
    public String getStrategyName() {
        return "LegalAnswerStrategy";
    }
}