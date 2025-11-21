package com.miapp.core.profile.web;

import com.miapp.core.profile.domain.UserProfile;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProfileResponse {
    private UUID userId;
    private String fullName;
    private String role;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProfileResponse() {
    }

    public ProfileResponse(UserProfile profile) {
        this.userId = profile.getUserId();
        this.fullName = profile.getFullName();
        this.role = profile.getRole();
        this.country = profile.getCountry();
        this.createdAt = profile.getCreatedAt();
        this.updatedAt = profile.getUpdatedAt();
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