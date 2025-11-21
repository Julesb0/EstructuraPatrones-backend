package com.miapp.core.web;

import com.miapp.core.domain.BusinessPlan;
import com.miapp.core.facade.BusinessPlanFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plans")
@CrossOrigin(origins = "*")
public class BusinessPlanController {
    private final BusinessPlanFacade businessPlanFacade;

    public BusinessPlanController(BusinessPlanFacade businessPlanFacade) {
        this.businessPlanFacade = businessPlanFacade;
    }

    @GetMapping
    public ResponseEntity<List<BusinessPlanResponse>> getUserPlans(@RequestHeader("Authorization") String authHeader) {
        try {
            String userId = extractUserIdFromToken(authHeader);
            List<BusinessPlan> plans = businessPlanFacade.getUserBusinessPlans(userId);
            
            List<BusinessPlanResponse> responses = plans.stream()
                .map(plan -> new BusinessPlanResponse(
                    plan.getId(),
                    plan.getTitle(),
                    plan.getSummary(),
                    plan.getCreatedAt(),
                    plan.getUpdatedAt()
                ))
                .collect(Collectors.toList());
                
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<BusinessPlanResponse> createPlan(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody BusinessPlanRequest request) {
        try {
            String userId = extractUserIdFromToken(authHeader);
            BusinessPlan plan = businessPlanFacade.createBusinessPlan(userId, request.getTitle(), request.getSummary());
            
            BusinessPlanResponse response = new BusinessPlanResponse(
                plan.getId(),
                plan.getTitle(),
                plan.getSummary(),
                plan.getCreatedAt(),
                plan.getUpdatedAt()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private String extractUserIdFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String[] parts = token.split("\\.");
                if (parts.length != 3) return null;
                
                String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(payload);
                return node.get("sub").asText();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}