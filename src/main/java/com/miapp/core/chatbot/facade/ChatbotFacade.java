package com.miapp.core.chatbot.facade;

import com.miapp.core.chatbot.nlp.LoggingNlpClient;
import com.miapp.core.chatbot.nlp.NlpClient;
import com.miapp.core.chatbot.nlp.NlpException;
import com.miapp.core.chatbot.service.CategoryDetector;
import com.miapp.core.chatbot.strategy.AnswerStrategyFactory;
import com.miapp.core.domain.ChatMessage;
import com.miapp.core.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación del patrón Facade
 * Proporciona una interfaz simplificada para toda la funcionalidad del chatbot
 * Configurado manualmente en NlpClientConfig
 */
public class ChatbotFacade {

    private final ChatMessageRepository chatMessageRepository;
    private final AnswerStrategyFactory strategyFactory;
    private final NlpClient nlpClient;
    private final CategoryDetector categoryDetector;

    @Autowired
    public ChatbotFacade(ChatMessageRepository chatMessageRepository,
                        AnswerStrategyFactory strategyFactory,
                        NlpClient loggingNlpClient,
                        CategoryDetector categoryDetector) {
        this.chatMessageRepository = chatMessageRepository;
        this.strategyFactory = strategyFactory;
        this.nlpClient = loggingNlpClient; // El cliente ya viene configurado con logging
        this.categoryDetector = categoryDetector;
    }

    /**
     * Procesa un mensaje del usuario y genera una respuesta
     * 
     * @param userId ID del usuario
     * @param userMessage Mensaje del usuario
     * @return La respuesta del chatbot
     */
    public ChatbotResponse processMessage(String userId, String userMessage) {
        try {
            // 1. Detectar categoría del mensaje
            String category = categoryDetector.detectCategory(userMessage);
            
            // 2. Guardar mensaje del usuario en la base de datos
            ChatMessage userChatMessage = new ChatMessage();
            userChatMessage.setUserId(userId);
            userChatMessage.setRole("USER");
            userChatMessage.setContent(userMessage);
            userChatMessage.setCategory(category);
            userChatMessage.setCreatedAt(LocalDateTime.now());
            
            chatMessageRepository.save(userChatMessage);
            
            // 3. Seleccionar estrategia de respuesta según la categoría
            var strategy = strategyFactory.getStrategy(category);
            
            // 4. Generar respuesta usando la estrategia seleccionada
            String botResponse;
            try {
                // Primero intentar con el cliente NLP externo
                botResponse = nlpClient.ask(userMessage);
            } catch (Exception e) {
                // Si falla el NLP externo, usar la estrategia local como respaldo
                botResponse = strategy.generateAnswer(userMessage);
            }
            
            // 5. Guardar respuesta del bot en la base de datos
            ChatMessage botChatMessage = new ChatMessage();
            botChatMessage.setUserId(userId);
            botChatMessage.setRole("BOT");
            botChatMessage.setContent(botResponse);
            botChatMessage.setCategory(category);
            botChatMessage.setCreatedAt(LocalDateTime.now());
            
            chatMessageRepository.save(botChatMessage);
            
            // 6. Retornar la respuesta
            return new ChatbotResponse(botResponse, category);
            
        } catch (Exception e) {
            throw new RuntimeException("Error processing chatbot message: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el historial de conversaciones de un usuario
     * 
     * @param userId ID del usuario
     * @return Lista de mensajes del chat
     */
    public List<ChatMessage> getUserChatHistory(String userId) {
        return chatMessageRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Obtiene el historial filtrado por categoría
     * 
     * @param userId ID del usuario
     * @param category Categoría a filtrar
     * @return Lista de mensajes filtrados
     */
    public List<ChatMessage> getUserChatHistoryByCategory(String userId, String category) {
        return chatMessageRepository.findByUserIdAndCategoryOrderByCreatedAtDesc(userId, category);
    }

    /**
     * DTO para la respuesta del chatbot
     */
    public static class ChatbotResponse {
        private final String reply;
        private final String category;

        public ChatbotResponse(String reply, String category) {
            this.reply = reply;
            this.category = category;
        }

        public String getReply() {
            return reply;
        }

        public String getCategory() {
            return category;
        }
    }
}