package com.emprendetube.restapi.controller;

import com.emprendetube.restapi.chatbot.facade.ChatbotFacade;
import com.emprendetube.restapi.entity.ChatMessage;
import com.emprendetube.restapi.entity.ChatMessageCategory;
import com.miapp.auth.service.JwtService;
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
    private JwtService jwtUtil;

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
            String userIdStr = jwtUtil.extractUserId(token);
            if (userIdStr == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
            }

            UUID userId = UUID.fromString(userIdStr);
            String message = request.get("message");

            if (message == null || message.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El mensaje no puede estar vacío"));
            }

            // Procesar el mensaje con el chatbot
            ChatbotFacade.ChatbotResponse response = chatbotFacade.processMessage(userId, message.trim());

            // Retornar la respuesta
            return ResponseEntity.ok(Map.of(
                "reply", response.getReply(),
                "category", response.getCategory().name(),
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
            String userIdStr = jwtUtil.extractUserId(token);
            if (userIdStr == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
            }

            UUID userId = UUID.fromString(userIdStr);

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
            String userIdStr = jwtUtil.extractUserId(token);
            if (userIdStr == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
            }

            UUID userId = UUID.fromString(userIdStr);

            // Validar y convertir la categoría
            ChatMessageCategory messageCategory;
            try {
                messageCategory = ChatMessageCategory.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Categoría inválida. Use: LEGAL, FINANCE, MARKETING, OTHER"
                ));
            }

            // Obtener el historial filtrado por categoría
            List<ChatMessage> history = chatbotFacade.getUserChatHistoryByCategory(userId, messageCategory);

            return ResponseEntity.ok(Map.of(
                "history", history,
                "category", category,
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
