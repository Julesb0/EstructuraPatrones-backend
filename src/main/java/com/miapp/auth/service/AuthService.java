package com.miapp.auth.service;

import com.miapp.auth.domain.AuthResponse;
import com.miapp.auth.domain.User;

public interface AuthService {
    AuthResponse register(String email, String password, String username) throws Exception;
    AuthResponse login(String email, String password) throws Exception;
    User getUserById(String userId) throws Exception;
}