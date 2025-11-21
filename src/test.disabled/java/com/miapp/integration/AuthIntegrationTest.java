package com.miapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miapp.auth.web.LoginRequest;
import com.miapp.auth.web.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterAndLoginFlow() throws Exception {
        // Test registration
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("integrationtest@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRecaptchaToken("test-recaptcha-token");
        
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());
        
        // Test login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("integrationtest@example.com");
        loginRequest.setPassword("password123");
        loginRequest.setRecaptchaToken("test-recaptcha-token");
        
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}