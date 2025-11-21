package com.miapp.core.chatbot.config;

import com.miapp.core.chatbot.facade.ChatbotFacade;
import com.miapp.core.chatbot.nlp.ExternalApiNlpClient;
import com.miapp.core.chatbot.nlp.FallbackNlpClient;
import com.miapp.core.chatbot.nlp.LoggingNlpClient;
import com.miapp.core.chatbot.nlp.NlpClient;
import com.miapp.core.chatbot.service.CategoryDetector;
import com.miapp.core.chatbot.strategy.AnswerStrategyFactory;
import com.miapp.core.repository.ChatMessageRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuración condicional del cliente NLP
 * Usa ExternalApiNlpClient si hay API key, FallbackNlpClient si no
 */
@Configuration
public class NlpClientConfig {
    
    @Bean
    @Primary
    public NlpClient nlpClient() {
        // Intentar cargar la API key
        String openAiApiKey = null;
        try {
            Dotenv dotenv = Dotenv.load();
            openAiApiKey = dotenv.get("OPENAI_API_KEY");
        } catch (Exception e) {
            // Si falla al cargar .env, intentar desde variable de entorno del sistema
            openAiApiKey = System.getenv("OPENAI_API_KEY");
        }
        
        // Si no hay API key, usar el cliente de respaldo
        if (openAiApiKey == null || openAiApiKey.trim().isEmpty()) {
            System.out.println("⚠️ No se encontró OPENAI_API_KEY, usando FallbackNlpClient");
            return new FallbackNlpClient();
        }
        
        System.out.println("✅ OPENAI_API_KEY encontrada, usando ExternalApiNlpClient");
        NlpClient externalClient = new ExternalApiNlpClient();
        return new LoggingNlpClient(externalClient);
    }
    
    @Bean(name = "loggingNlpClient")
    public NlpClient loggingNlpClient(NlpClient nlpClient) {
        return new LoggingNlpClient(nlpClient);
    }
    
    @Bean
    public ChatbotFacade chatbotFacade(ChatMessageRepository chatMessageRepository,
                                     AnswerStrategyFactory strategyFactory,
                                     @Qualifier("loggingNlpClient") NlpClient loggingNlpClient,
                                     CategoryDetector categoryDetector) {
        return new ChatbotFacade(chatMessageRepository, strategyFactory, loggingNlpClient, categoryDetector);
    }
}