package com.miapp.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private final String testSecret = "test-secret-key-that-is-at-least-256-bits-long-for-testing-purposes";
    private final String testUserId = "test-user-id-123";
    private final String testUsername = "testuser";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secret", testSecret);
        ReflectionTestUtils.setField(jwtService, "expiration", 86400000L); // 24 hours
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(testUserId, testUsername);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUserId() {
        String token = jwtService.generateToken(testUserId, testUsername);
        String extractedUserId = jwtService.extractUserId(token);
        
        assertEquals(testUserId, extractedUserId);
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken(testUserId, testUsername);
        String extractedUsername = jwtService.extractUsername(token);
        
        assertEquals(testUsername, extractedUsername);
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = jwtService.generateToken(testUserId, testUsername);
        boolean isValid = jwtService.validateToken(token);
        
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidToken() {
        String invalidToken = "invalid-token-format";
        boolean isValid = jwtService.validateToken(invalidToken);
        
        assertFalse(isValid);
    }

    @Test
    void testIsTokenExpired_ValidToken() {
        String token = jwtService.generateToken(testUserId, testUsername);
        boolean isExpired = jwtService.isTokenExpired(token);
        
        assertFalse(isExpired);
    }

    @Test
    void testCompleteTokenFlow() {
        // Generate token
        String token = jwtService.generateToken(testUserId, testUsername);
        assertNotNull(token);
        
        // Validate token
        assertTrue(jwtService.validateToken(token));
        
        // Extract user info
        assertEquals(testUserId, jwtService.extractUserId(token));
        assertEquals(testUsername, jwtService.extractUsername(token));
        
        // Check expiration
        assertFalse(jwtService.isTokenExpired(token));
    }
}