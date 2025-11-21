package com.emprendetube.restapi.chatbot.strategy;

import com.emprendetube.restapi.entity.ChatMessageCategory;
import org.springframework.stereotype.Component;

@Component
public class FinanceAnswerStrategy implements AnswerStrategy {

    @Override
    public boolean canHandle(ChatMessageCategory category) {
        return category == ChatMessageCategory.FINANCE;
    }

    @Override
    public String generateAnswer(String userMessage) {
        // Respuestas simuladas para temas financieros
        if (userMessage.toLowerCase().contains("inversión") || userMessage.toLowerCase().contains("inversion")) {
            return "Para inversiones, considera diversificar tu portafolio, evaluar el riesgo y establecer un horizonte temporal claro. Busca asesoramiento profesional para decisiones importantes.";
        } else if (userMessage.toLowerCase().contains("presupuesto")) {
            return "Un buen presupuesto empresarial debe incluir ingresos proyectados, gastos fijos y variables, y un colchón para imprevistos. Revisa y ajusta mensualmente.";
        } else if (userMessage.toLowerCase().contains("cash flow") || userMessage.toLowerCase().contains("flujo")) {
            return "El flujo de caja es vital. Controla tus cuentas por cobrar y pagar, mantén reservas para emergencias, y considera líneas de crédito para momentos de escasez.";
        } else if (userMessage.toLowerCase().contains("financiamiento") || userMessage.toLowerCase().contains("financiación")) {
            return "Las opciones de financiamiento incluyen préstamos bancarios, inversión de ángeles, crowdfunding o bootstrapping. Evalúa tasas de interés y impacto en tu capital.";
        } else {
            return "Para temas financieros específicos, te recomiendo consultar con un asesor financiero que pueda analizar tu situación particular y objetivos comerciales.";
        }
    }

    @Override
    public String getStrategyName() {
        return "FinanceAnswerStrategy";
    }
}