package com.miapp.auth.web;

public class RegisterRequest {
    private String email;
    private String password;
    private String username;
    private String recaptchaToken;

    public RegisterRequest() {}

    public RegisterRequest(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public RegisterRequest(String email, String password, String username, String recaptchaToken) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.recaptchaToken = recaptchaToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRecaptchaToken() {
        return recaptchaToken;
    }

    public void setRecaptchaToken(String recaptchaToken) {
        this.recaptchaToken = recaptchaToken;
    }
}