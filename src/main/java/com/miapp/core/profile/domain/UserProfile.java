package com.miapp.core.profile.domain;

import java.time.LocalDateTime;

/**
 * UserProfile entity representing entrepreneur profiles
 * Maps to the 'profiles' table in Supabase
 */
public class UserProfile {
    private String userId;      // UUID, references auth.users(id)
    private String fullName;    // User's full name
    private String role;        // ENTREPRENEUR, MENTOR, INVESTOR, ADMIN
    private String country;     // User's country
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserProfile() {}

    public UserProfile(String userId, String fullName, String role, String country) {
        this.userId = userId;
        this.fullName = fullName;
        this.role = role;
        this.country = country;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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
        return "UserProfile{" +
                "userId='" + userId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                ", country='" + country + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}