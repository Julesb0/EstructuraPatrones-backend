package com.emprendetube.restapi.chatbot.strategy;

import com.emprendetube.restapi.entity.ChatMessageCategory;
import org.springframework.stereotype.Component;

@Component
public class LegalAnswerStrategy implements AnswerStrategy {

    @Override
    public boolean canHandle(ChatMessageCategory category) {
        return category == ChatMessageCategory.LEGAL;
    }

    @Override
    public String generateAnswer(String userMessage) {
        // Respuestas simuladas para temas legales
        if (userMessage.toLowerCase().contains("contrato")) {
            return "Para temas de contratos, te recomiendo consultar con un abogado especializado. Es importante tener cláusulas claras sobre responsabilidades, plazos y condiciones de pago.";
        } else if (userMessage.toLowerCase().contains("registro")) {
            return "El registro de tu empresa es fundamental. Necesitas definir la estructura legal (SL, SA, autónomo) y registrarla en el registro mercantil correspondiente.";
        } else if (userMessage.toLowerCase().contains("impuesto")) {
            return "Los impuestos varían según la estructura legal de tu empresa. Es recomendable contratar a un asesor fiscal para optimizar tu carga tributaria.";
        } else if (userMessage.toLowerCase().contains("propiedad intelectual")) {
            return "La protección de propiedad intelectual es crucial. Considera registrar marcas, patentes y derechos de autor para proteger tus innovaciones.";
        } else {
            return "Para asuntos legales específicos, te sugiero consultar con un profesional del derecho mercantil que pueda asesorarte según tu situación particular.";
        }
    }

    @Override
    public String getStrategyName() {
        return "LegalAnswerStrategy";
    }
}