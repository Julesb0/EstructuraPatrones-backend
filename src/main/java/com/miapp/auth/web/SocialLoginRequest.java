package com.miapp.auth.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SocialLoginRequest {
    
    @NotBlank(message = "Access token is required")
    private String accessToken;
    
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotNull(message = "Provider is required")
    private String provider;

    public SocialLoginRequest() {}

    public SocialLoginRequest(String accessToken, String email, String provider) {
        this.accessToken = accessToken;
        this.email = email;
        this.provider = provider;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}