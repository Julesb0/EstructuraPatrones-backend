package com.miapp.core.domain;

import java.time.LocalDateTime;

public class ChatMessage {
    private String id;
    private String userId;
    private String role;
    private String content;
    private String category;
    private LocalDateTime createdAt;

    public ChatMessage() {}

    public ChatMessage(String id, String userId, String role, String content, String category, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.role = role;
        this.content = content;
        this.category = category;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}