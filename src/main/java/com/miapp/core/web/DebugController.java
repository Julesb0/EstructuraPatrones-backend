package com.miapp.core.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {
    
    @Autowired
    private Environment env;
    
    @GetMapping("/config")
    public Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<>();
        
        // Mostrar configuración de Supabase (sin mostrar las claves secretas)
        config.put("supabaseUrl", env.getProperty("supabase.url", "NOT_SET"));
        config.put("supabaseAnonKeySet", env.getProperty("supabase.anonKey", "NOT_SET").equals("NOT_SET") ? false : true);
        config.put("supabaseServiceRoleKeySet", env.getProperty("supabase.serviceRoleKey", "NOT_SET").equals("NOT_SET") ? false : true);
        
        // Configuración JWT
        config.put("jwtSecretSet", env.getProperty("jwt.secret", "NOT_SET").equals("NOT_SET") ? false : true);
        config.put("jwtExpiration", env.getProperty("jwt.expiration", "NOT_SET"));
        
        // Configuración de recaptcha
        config.put("recaptchaSecretKey", env.getProperty("recaptcha.secretKey", "NOT_SET"));
        
        // Perfil activo
        config.put("activeProfiles", env.getActiveProfiles());
        
        return config;
    }
}