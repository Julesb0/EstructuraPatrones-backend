package com.miapp.core.chatbot.strategy;

import org.springframework.stereotype.Component;

/**
 * Implementación de estrategia para temas financieros
 */
@Component
public class FinanceAnswerStrategy implements AnswerStrategy {
    
    @Override
    public String generateAnswer(String question) {
        if (question.toLowerCase().contains("financiamiento") || question.toLowerCase().contains("inversión")) {
            return "Para el financiamiento de tu empresa, considera opciones como: préstamos bancarios, inversión de ángeles, capital de riesgo, crowdfunding o bootstrapping. Cada opción tiene sus ventajas y desventajas según tu etapa de negocio.";
        } else if (question.toLowerCase().contains("flujo") || question.toLowerCase().contains("caja")) {
            return "El flujo de caja es vital para la supervivencia de tu empresa. Mantén un control estricto de tus ingresos y egresos, y considera mantener un colchón de efectivo para emergencias.";
        } else if (question.toLowerCase().contains("presupuesto") || question.toLowerCase().contains("plan")) {
            return "Un buen presupuesto es la base de la salud financiera de tu empresa. Asegúrate de incluir todos los costos fijos y variables, y revisa mensualmente tu desempeño contra el presupuesto.";
        } else {
            return "Para temas financieros específicos, te recomiendo consultar con un asesor financiero o contador profesional. Puedo ayudarte con conceptos generales y orientación básica.";
        }
    }
    
    @Override
    public boolean canHandle(String question) {
        String lowerQuestion = question.toLowerCase();
        return lowerQuestion.contains("financ") || lowerQuestion.contains("dinero") || 
               lowerQuestion.contains("inversión") || lowerQuestion.contains("inversion") ||
               lowerQuestion.contains("flujo") || lowerQuestion.contains("caja") ||
               lowerQuestion.contains("presupuesto") || lowerQuestion.contains("plan");
    }
    
    @Override
    public String getStrategyName() {
        return "FinanceAnswerStrategy";
    }
}