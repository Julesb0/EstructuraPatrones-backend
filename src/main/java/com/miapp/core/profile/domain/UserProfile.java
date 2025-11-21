package com.miapp.core.profile.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserProfile {
    private UUID userId;
    private String fullName;
    private String role;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserProfile() {
    }

    public UserProfile(UUID userId, String fullName, String role, String country) {
        this.userId = userId;
        this.fullName = fullName;
        this.role = role;
        this.country = country;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}