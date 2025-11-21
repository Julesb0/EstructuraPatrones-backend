package com.miapp.core.chatbot.strategy;

import org.springframework.stereotype.Component;

/**
 * Implementación de estrategia para temas de marketing
 */
@Component
public class MarketingAnswerStrategy implements AnswerStrategy {
    
    @Override
    public String generateAnswer(String question) {
        if (question.toLowerCase().contains("marketing") || question.toLowerCase().contains("publicidad")) {
            return "El marketing digital es esencial hoy en día. Considera usar redes sociales, marketing de contenidos, SEO y email marketing. Define bien tu público objetivo y crea contenido relevante para ellos.";
        } else if (question.toLowerCase().contains("cliente") || question.toLowerCase().contains("venta")) {
            return "Conocer a tu cliente ideal es fundamental. Investiga sus necesidades, preferencias y comportamientos. Esto te ayudará a crear ofertas más atractivas y mejorar tus ventas.";
        } else if (question.toLowerCase().contains("marca") || question.toLowerCase().contains("posicionamiento")) {
            return "Tu marca es mucho más que un logo. Es la percepción que tienen los clientes sobre tu empresa. Trabaja en construir una identidad de marca coherente y memorable.";
        } else {
            return "Para temas de marketing específicos, te recomiendo investigar las últimas tendencias y considerar contratar un especialista en marketing digital. Puedo ayudarte con conceptos generales.";
        }
    }
    
    @Override
    public boolean canHandle(String question) {
        String lowerQuestion = question.toLowerCase();
        return lowerQuestion.contains("marketing") || lowerQuestion.contains("publicidad") || 
               lowerQuestion.contains("cliente") || lowerQuestion.contains("venta") ||
               lowerQuestion.contains("marca") || lowerQuestion.contains("posicionamiento");
    }
    
    @Override
    public String getStrategyName() {
        return "MarketingAnswerStrategy";
    }
}