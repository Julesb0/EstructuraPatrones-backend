package com.emprendetube.restapi.repository;

import com.emprendetube.restapi.entity.ChatMessage;
import com.emprendetube.restapi.entity.ChatMessageCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import io.github.cdimascio.dotenv.Dotenv;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class ChatMessageRepository {

    @Autowired
    private SupabaseClient supabaseClient;

    public ChatMessage save(ChatMessage message) {
        try {
            if (message.getId() == null) {
                message.setId(UUID.randomUUID());
            }
            if (message.getCreatedAt() == null) {
                message.setCreatedAt(LocalDateTime.now());
            }

            String sql = "INSERT INTO chat_messages (id, user_id, role, content, category, created_at) VALUES (?, ?, ?, ?, ?, ?)";
            
            supabaseClient.execute(sql, 
                message.getId(),
                message.getUserId(),
                message.getRole().name(),
                message.getContent(),
                message.getCategory().name(),
                message.getCreatedAt()
            );

            return message;
        } catch (Exception e) {
            throw new RuntimeException("Error saving chat message: " + e.getMessage(), e);
        }
    }

    public List<ChatMessage> findByUserIdOrderByCreatedAtDesc(UUID userId) {
        try {
            String sql = "SELECT * FROM chat_messages WHERE user_id = ? ORDER BY created_at DESC";
            
            return supabaseClient.query(sql, ChatMessage.class, userId);
        } catch (Exception e) {
            throw new RuntimeException("Error finding chat messages by user ID: " + e.getMessage(), e);
        }
    }

    public List<ChatMessage> findByUserIdAndCategoryOrderByCreatedAtDesc(UUID userId, ChatMessageCategory category) {
        try {
            String sql = "SELECT * FROM chat_messages WHERE user_id = ? AND category = ? ORDER BY created_at DESC";
            
            return supabaseClient.query(sql, ChatMessage.class, userId, category.name());
        } catch (Exception e) {
            throw new RuntimeException("Error finding chat messages by user ID and category: " + e.getMessage(), e);
        }
    }

    public List<ChatMessage> findRecentByUserId(UUID userId, int limit) {
        try {
            String sql = "SELECT * FROM chat_messages WHERE user_id = ? ORDER BY created_at DESC LIMIT ?";
            
            return supabaseClient.query(sql, ChatMessage.class, userId, limit);
        } catch (Exception e) {
            throw new RuntimeException("Error finding recent chat messages: " + e.getMessage(), e);
        }
    }
}