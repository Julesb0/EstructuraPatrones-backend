package com.miapp.core.profile.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for profile update requests
 */
public class ProfileUpdateRequest {
    
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    
    @NotBlank(message = "Role is required")
    private String role; // ENTREPRENEUR, MENTOR, INVESTOR, ADMIN
    
    @Size(max = 50, message = "Country name must not exceed 50 characters")
    private String country;

    public ProfileUpdateRequest() {}

    public ProfileUpdateRequest(String fullName, String role, String country) {
        this.fullName = fullName;
        this.role = role;
        this.country = country;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "ProfileUpdateRequest{" +
                "fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}