package com.emprendetube.restapi.chatbot.facade;

import com.emprendetube.restapi.chatbot.nlp.LoggingNlpClient;
import com.emprendetube.restapi.chatbot.nlp.NlpClient;
import com.emprendetube.restapi.chatbot.nlp.NlpException;
import com.emprendetube.restapi.chatbot.service.CategoryDetector;
import com.emprendetube.restapi.chatbot.strategy.AnswerStrategyFactory;
import com.emprendetube.restapi.entity.ChatMessage;
import com.emprendetube.restapi.entity.ChatMessageCategory;
import com.emprendetube.restapi.entity.ChatMessageRole;
import com.emprendetube.restapi.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementación del patrón Facade
 * Proporciona una interfaz simplificada para toda la funcionalidad del chatbot
 */
@Component
public class ChatbotFacade {

    private final ChatMessageRepository chatMessageRepository;
    private final AnswerStrategyFactory strategyFactory;
    private final NlpClient nlpClient;
    private final CategoryDetector categoryDetector;

    @Autowired
    public ChatbotFacade(ChatMessageRepository chatMessageRepository,
                        AnswerStrategyFactory strategyFactory,
                        NlpClient nlpClient,
                        CategoryDetector categoryDetector) {
        this.chatMessageRepository = chatMessageRepository;
        this.strategyFactory = strategyFactory;
        this.nlpClient = new LoggingNlpClient(nlpClient); // Aplicar el patrón Decorator
        this.categoryDetector = categoryDetector;
    }

    /**
     * Procesa un mensaje del usuario y genera una respuesta
     * 
     * @param userId ID del usuario
     * @param userMessage Mensaje del usuario
     * @return La respuesta del chatbot
     */
    public ChatbotResponse processMessage(UUID userId, String userMessage) {
        try {
            // 1. Detectar categoría del mensaje
            ChatMessageCategory category = categoryDetector.detectCategory(userMessage);
            
            // 2. Guardar mensaje del usuario en la base de datos
            ChatMessage userChatMessage = new ChatMessage();
            userChatMessage.setUserId(userId);
            userChatMessage.setRole(ChatMessageRole.USER);
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
            botChatMessage.setRole(ChatMessageRole.BOT);
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
    public List<ChatMessage> getUserChatHistory(UUID userId) {
        return chatMessageRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Obtiene el historial filtrado por categoría
     * 
     * @param userId ID del usuario
     * @param category Categoría a filtrar
     * @return Lista de mensajes filtrados
     */
    public List<ChatMessage> getUserChatHistoryByCategory(UUID userId, ChatMessageCategory category) {
        return chatMessageRepository.findByUserIdAndCategoryOrderByCreatedAtDesc(userId, category);
    }

    /**
     * DTO para la respuesta del chatbot
     */
    public static class ChatbotResponse {
        private final String reply;
        private final ChatMessageCategory category;

        public ChatbotResponse(String reply, ChatMessageCategory category) {
            this.reply = reply;
            this.category = category;
        }

        public String getReply() {
            return reply;
        }

        public ChatMessageCategory getCategory() {
            return category;
        }
    }
}