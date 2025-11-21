package com.miapp.core.web;

import com.miapp.auth.service.JwtService;
import com.miapp.core.chatbot.facade.ChatbotFacade;
import com.miapp.core.domain.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*")
public class ChatbotController {

    @Autowired
    private ChatbotFacade chatbotFacade;

    @Autowired
    private JwtService jwtService;

    /**
     * Endpoint para enviar un mensaje al chatbot
     * POST /api/chatbot/messages
     */
    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> request, 
                                       HttpServletRequest httpRequest) {
        try {
            // Obtener el token JWT del header
            String token = extractToken(httpRequest);
            if (token == null) {
                return ResponseEntity.status(401).body(Map.of("error", "No autorizado"));
            }

            // Extraer userId del token
            String userId = jwtService.extractUserId(token);
            if (userId == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
            }

            String message = request.get("message");

            if (message == null || message.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El mensaje no puede estar vacío"));
            }

            // Procesar el mensaje con el chatbot
            ChatbotFacade.ChatbotResponse response = chatbotFacade.processMessage(userId, message.trim());

            // Retornar la respuesta
            return ResponseEntity.ok(Map.of(
                "reply", response.getReply(),
                "category", response.getCategory(),
                "success", true
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "Error al procesar el mensaje: " + e.getMessage(),
                "success", false
            ));
        }
    }

    /**
     * Endpoint para obtener el historial de conversaciones
     * GET /api/chatbot/history
     */
    @GetMapping("/history")
    public ResponseEntity<?> getChatHistory(HttpServletRequest httpRequest) {
        try {
            // Obtener el token JWT del header
            String token = extractToken(httpRequest);
            if (token == null) {
                return ResponseEntity.status(401).body(Map.of("error", "No autorizado"));
            }

            // Extraer userId del token
            String userId = jwtService.extractUserId(token);
            if (userId == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
            }

            // Obtener el historial de conversaciones
            List<ChatMessage> history = chatbotFacade.getUserChatHistory(userId);

            return ResponseEntity.ok(Map.of(
                "history", history,
                "success", true
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "Error al obtener el historial: " + e.getMessage(),
                "success", false
            ));
        }
    }

    /**
     * Endpoint para obtener el historial filtrado por categoría
     * GET /api/chatbot/history/{category}
     */
    @GetMapping("/history/{category}")
    public ResponseEntity<?> getChatHistoryByCategory(@PathVariable String category, 
                                                    HttpServletRequest httpRequest) {
        try {
            // Obtener el token JWT del header
            String token = extractToken(httpRequest);
            if (token == null) {
                return ResponseEntity.status(401).body(Map.of("error", "No autorizado"));
            }

            // Extraer userId del token
            String userId = jwtService.extractUserId(token);
            if (userId == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
            }

            // Validar y convertir la categoría
            String upperCategory;
            try {
                upperCategory = category.toUpperCase();
                if (!upperCategory.matches("LEGAL|FINANCE|MARKETING|OTHER")) {
                    return ResponseEntity.badRequest().body(Map.of(
                        "error", "Categoría inválida. Use: legal, finance, marketing, other"
                    ));
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Categoría inválida. Use: legal, finance, marketing, other"
                ));
            }

            // Obtener el historial filtrado por categoría
            List<ChatMessage> history = chatbotFacade.getUserChatHistoryByCategory(userId, upperCategory);

            return ResponseEntity.ok(Map.of(
                "history", history,
                "category", upperCategory,
                "success", true
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "Error al obtener el historial: " + e.getMessage(),
                "success", false
            ));
        }
    }

    /**
     * Extrae el token JWT del header Authorization
     */
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}