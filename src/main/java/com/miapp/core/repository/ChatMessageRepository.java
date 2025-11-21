package com.miapp.core.repository;

import com.miapp.core.domain.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class ChatMessageRepository {
    
    private static final String TABLE_NAME = "chat_messages";
    
    @Autowired
    private SupabaseRepository supabaseRepository;
    
    public ChatMessage save(ChatMessage message) {
        try {
            if (message.getId() == null) {
                message.setId(UUID.randomUUID().toString());
            }
            if (message.getCreatedAt() == null) {
                message.setCreatedAt(LocalDateTime.now());
            }
            
            return supabaseRepository.save(TABLE_NAME, message, ChatMessage.class);
        } catch (Exception e) {
            throw new RuntimeException("Error saving chat message: " + e.getMessage(), e);
        }
    }
    
    public List<ChatMessage> findByUserIdOrderByCreatedAtDesc(String userId) {
        try {
            String filter = "user_id=eq." + userId;
            String order = "created_at.desc";
            return supabaseRepository.findAllWithFilter(TABLE_NAME, filter, order, ChatMessage.class);
        } catch (Exception e) {
            throw new RuntimeException("Error finding chat messages by user ID: " + e.getMessage(), e);
        }
    }
    
    public List<ChatMessage> findByUserIdAndCategoryOrderByCreatedAtDesc(String userId, String category) {
        try {
            String filter = "user_id=eq." + userId + "&category=eq." + category;
            String order = "created_at.desc";
            return supabaseRepository.findAllWithFilter(TABLE_NAME, filter, order, ChatMessage.class);
        } catch (Exception e) {
            throw new RuntimeException("Error finding chat messages by user ID and category: " + e.getMessage(), e);
        }
    }
    
    public List<ChatMessage> findRecentByUserId(String userId, int limit) {
        try {
            String filter = "user_id=eq." + userId;
            String order = "created_at.desc";
            String range = "0-" + (limit - 1);
            return supabaseRepository.findAllWithFilterAndRange(TABLE_NAME, filter, order, range, ChatMessage.class);
        } catch (Exception e) {
            throw new RuntimeException("Error finding recent chat messages: " + e.getMessage(), e);
        }
    }
}