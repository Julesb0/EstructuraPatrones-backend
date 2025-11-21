package com.emprendetube.restapi.chatbot.service;

import com.emprendetube.restapi.entity.ChatMessageCategory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CategoryDetector {

    private static final List<String> LEGAL_KEYWORDS = Arrays.asList(
        "legal", "ley", "contrato", "registro", "impuesto", "fiscal", "propiedad intelectual",
        "marca", "patente", "sociedad", "empresa", "constitución", "estatutos", "licencia",
        "permiso", "normativa", "regulación", "compliance", "gdpr", "protección de datos"
    );

    private static final List<String> FINANCE_KEYWORDS = Arrays.asList(
        "finanzas", "dinero", "inversión", "inversion", "presupuesto", "cash flow", "flujo",
        "financiamiento", "financiación", "crédito", "préstamo", "banco", "contabilidad",
        "beneficio", "pérdida", "ingreso", "gasto", "roi", "rentabilidad", "capital",
        "deuda", "factura", "cobro", "pago"
    );

    private static final List<String> MARKETING_KEYWORDS = Arrays.asList(
        "marketing", "publicidad", "anuncio", "seo", "sem", "redes sociales", "social media",
        "contenido", "branding", "marca", "cliente", "ventas", "promoción", "campaña",
        "estrategia", "posicionamiento", "mercado", "competencia", "producto", "servicio"
    );

    public ChatMessageCategory detectCategory(String message) {
        String lowerMessage = message.toLowerCase();
        
        // Contar palabras clave por categoría
        int legalCount = countKeywords(lowerMessage, LEGAL_KEYWORDS);
        int financeCount = countKeywords(lowerMessage, FINANCE_KEYWORDS);
        int marketingCount = countKeywords(lowerMessage, MARKETING_KEYWORDS);

        // Determinar la categoría con más coincidencias
        if (legalCount >= financeCount && legalCount >= marketingCount && legalCount > 0) {
            return ChatMessageCategory.LEGAL;
        } else if (financeCount >= legalCount && financeCount >= marketingCount && financeCount > 0) {
            return ChatMessageCategory.FINANCE;
        } else if (marketingCount >= legalCount && marketingCount >= financeCount && marketingCount > 0) {
            return ChatMessageCategory.MARKETING;
        } else {
            return ChatMessageCategory.OTHER;
        }
    }

    private int countKeywords(String message, List<String> keywords) {
        int count = 0;
        for (String keyword : keywords) {
            if (message.contains(keyword.toLowerCase())) {
                count++;
            }
        }
        return count;
    }
}