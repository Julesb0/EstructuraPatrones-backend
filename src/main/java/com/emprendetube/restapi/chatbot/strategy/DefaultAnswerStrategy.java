package com.emprendetube.restapi.chatbot.strategy;

import com.emprendetube.restapi.entity.ChatMessageCategory;
import org.springframework.stereotype.Component;

@Component
public class DefaultAnswerStrategy implements AnswerStrategy {

    @Override
    public boolean canHandle(ChatMessageCategory category) {
        return category == ChatMessageCategory.OTHER;
    }

    @Override
    public String generateAnswer(String userMessage) {
        // Respuestas genéricas para temas no categorizados
        return "Gracias por tu pregunta. Como asistente para emprendedores, puedo ayudarte con temas de:"
                + "\n\n• LEGALES: Contratos, registros de empresa, propiedad intelectual"
                + "\n• FINANZAS: Presupuestos, inversiones, flujo de caja, financiamiento"
                + "\n• MARKETING: Redes sociales, SEO, publicidad, contenido"
                + "\n\n¿Podrías especificar a qué área se refiere tu consulta? Así podré darte una respuesta más precisa.";
    }

    @Override
    public String getStrategyName() {
        return "DefaultAnswerStrategy";
    }
}