package com.miapp.core.profile.web;

import java.time.LocalDateTime;

/**
 * DTO for profile responses
 */
public class ProfileResponse {
    private String userId;
    private String fullName;
    private String role;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProfileResponse() {}

    public ProfileResponse(String userId, String fullName, String role, String country, 
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.role = role;
        this.country = country;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    @Override
    public String toString() {
        return "ProfileResponse{" +
                "userId='" + userId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                ", country='" + country + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}