package com.emprendetube.restapi.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChatMessage {
    private UUID id;
    private UUID userId;
    private ChatMessageRole role;
    private String content;
    private ChatMessageCategory category;
    private LocalDateTime createdAt;

    public ChatMessage() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public ChatMessageRole getRole() {
        return role;
    }

    public void setRole(ChatMessageRole role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ChatMessageCategory getCategory() {
        return category;
    }

    public void setCategory(ChatMessageCategory category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
