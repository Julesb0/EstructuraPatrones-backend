package com.miapp.core.chatbot.strategy;

import org.springframework.stereotype.Component;

/**
 * Estrategia por defecto cuando ninguna otra estrategia puede manejar la pregunta
 */
@Component
public class DefaultAnswerStrategy implements AnswerStrategy {
    
    @Override
    public String generateAnswer(String question) {
        if (question.toLowerCase().contains("hola") || question.toLowerCase().contains("buenos")) {
            return "¡Hola! Soy tu asistente emprendedor virtual. Estoy aquí para ayudarte con temas legales, financieros y de marketing. ¿En qué puedo asistirte hoy?";
        } else if (question.length() < 10) {
            return "Gracias por tu mensaje. Como asistente para emprendedores, puedo ayudarte con temas legales, financieros y de marketing. ¿Podrías darme más detalles sobre tu consulta?";
        } else {
            return "Entiendo tu consulta. Como asistente emprendedor, te sugiero que analices bien tu situación actual. Si necesitas ayuda específica con aspectos legales, financieros o de marketing, estaré encantado de ayudarte. ¿Hay algo más específico sobre lo que necesitas orientación?";
        }
    }
    
    @Override
    public boolean canHandle(String question) {
        // Esta estrategia puede manejar cualquier pregunta como respaldo
        return true;
    }
    
    @Override
    public String getStrategyName() {
        return "DefaultAnswerStrategy";
    }
}