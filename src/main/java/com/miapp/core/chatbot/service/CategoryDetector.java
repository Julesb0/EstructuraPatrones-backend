package com.miapp.core.chatbot.service;

import org.springframework.stereotype.Service;

/**
 * Servicio para detectar la categoría de un mensaje basándose en su contenido
 */
@Service
public class CategoryDetector {
    
    /**
     * Detecta la categoría del mensaje basándose en palabras clave
     */
    public String detectCategory(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "OTHER";
        }
        
        String lowerMessage = message.toLowerCase();
        
        // Categoría LEGAL
        if (lowerMessage.contains("legal") || lowerMessage.contains("ley") || 
            lowerMessage.contains("registro") || lowerMessage.contains("empresa") ||
            lowerMessage.contains("contrato") || lowerMessage.contains("socio") ||
            lowerMessage.contains("impuesto") || lowerMessage.contains("iva")) {
            return "LEGAL";
        }
        
        // Categoría FINANCE
        if (lowerMessage.contains("financ") || lowerMessage.contains("dinero") || 
            lowerMessage.contains("inversión") || lowerMessage.contains("inversion") ||
            lowerMessage.contains("flujo") || lowerMessage.contains("caja") ||
            lowerMessage.contains("presupuesto") || lowerMessage.contains("plan")) {
            return "FINANCE";
        }
        
        // Categoría MARKETING
        if (lowerMessage.contains("marketing") || lowerMessage.contains("publicidad") || 
            lowerMessage.contains("cliente") || lowerMessage.contains("venta") ||
            lowerMessage.contains("marca") || lowerMessage.contains("posicionamiento")) {
            return "MARKETING";
        }
        
        // Categoría OTHER por defecto
        return "OTHER";
    }
}