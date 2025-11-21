package com.miapp.auth.factory;

import com.miapp.auth.service.AuthService;
import com.miapp.auth.service.SupabaseAuthAdapter;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceFactory {
    private final SupabaseAuthAdapter supabaseAuthAdapter;

    public AuthServiceFactory(SupabaseAuthAdapter supabaseAuthAdapter) {
        this.supabaseAuthAdapter = supabaseAuthAdapter;
    }

    public AuthService createAuthService() {
        return supabaseAuthAdapter;
    }
}