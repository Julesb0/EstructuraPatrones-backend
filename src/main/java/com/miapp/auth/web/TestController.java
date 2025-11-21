package com.miapp.auth.web;

import com.miapp.auth.service.RecaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @Autowired
    private RecaptchaService recaptchaService;

    @PostMapping("/recaptcha")
    public ResponseEntity<?> testRecaptcha(@RequestBody Map<String, String> request) {
        String recaptchaToken = request.get("recaptchaToken");
        
        if (recaptchaToken == null || recaptchaToken.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "reCAPTCHA token is required"
            ));
        }
        
        boolean isValid = recaptchaService.isValid(recaptchaToken);
        
        if (isValid) {
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "reCAPTCHA validation successful"
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "reCAPTCHA validation failed"
            ));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
            "status", "healthy",
            "recaptcha", "configured"
        ));
    }

    @GetMapping("/simple")
    public ResponseEntity<?> simpleTest() {
        return ResponseEntity.ok(Map.of(
            "message", "Backend is working!",
            "timestamp", System.currentTimeMillis()
        ));
    }
}