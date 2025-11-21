package com.emprendetube.restapi.chatbot.strategy;

import com.emprendetube.restapi.entity.ChatMessageCategory;
import org.springframework.stereotype.Component;

@Component
public class MarketingAnswerStrategy implements AnswerStrategy {

    @Override
    public boolean canHandle(ChatMessageCategory category) {
        return category == ChatMessageCategory.MARKETING;
    }

    @Override
    public String generateAnswer(String userMessage) {
        // Respuestas simuladas para temas de marketing
        if (userMessage.toLowerCase().contains("redes sociales") || userMessage.toLowerCase().contains("social media")) {
            return "Para redes sociales, define tu audiencia objetivo, crea contenido valioso y consistente, y utiliza analytics para medir el rendimiento. Considera plataformas donde esté tu público objetivo.";
        } else if (userMessage.toLowerCase().contains("seo")) {
            return "El SEO es fundamental para visibilidad orgánica. Optimiza tus páginas con keywords relevantes, crea contenido de calidad y asegúrate de que tu sitio sea móvil-friendly y rápido.";
        } else if (userMessage.toLowerCase().contains("publicidad") || userMessage.toLowerCase().contains("ads")) {
            return "La publicidad pagada puede acelerar tu crecimiento. Define claramente tu público objetivo, establece un presupuesto y monitorea constantemente el ROI de tus campañas.";
        } else if (userMessage.toLowerCase().contains("contenido")) {
            return "El contenido de calidad es clave. Conoce a tu audiencia, resuelve sus problemas, y mantén consistencia en tu mensaje y frecuencia de publicación.";
        } else {
            return "Para estrategias de marketing específicas, te recomiendo definir claramente tu buyer persona y objetivos comerciales antes de seleccionar los canales y tácticas apropiadas.";
        }
    }

    @Override
    public String getStrategyName() {
        return "MarketingAnswerStrategy";
    }
}