package com.miapp.core.service;

import com.miapp.core.domain.BusinessPlan;
import com.miapp.core.repository.BusinessPlanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BusinessPlanService {
    private final BusinessPlanRepository businessPlanRepository;

    public BusinessPlanService(BusinessPlanRepository businessPlanRepository) {
        this.businessPlanRepository = businessPlanRepository;
    }

    public List<BusinessPlan> getBusinessPlansByUserId(String userId) throws Exception {
        return businessPlanRepository.findByUserId(userId);
    }

    public BusinessPlan createBusinessPlan(String userId, String title, String summary) throws Exception {
        BusinessPlan plan = new BusinessPlan();
        plan.setId(UUID.randomUUID().toString());
        plan.setUserId(userId);
        plan.setTitle(title);
        plan.setSummary(summary);
        plan.setStatus("in_progress"); // Estado por defecto
        plan.setCreatedAt(LocalDateTime.now());
        plan.setUpdatedAt(LocalDateTime.now());
        
        return businessPlanRepository.save(plan);
    }

    public BusinessPlan updateBusinessPlan(String userId, String planId, String title, String summary) throws Exception {
        // Primero verificar que el plan existe y pertenece al usuario
        List<BusinessPlan> userPlans = businessPlanRepository.findByUserId(userId);
        BusinessPlan existingPlan = userPlans.stream()
            .filter(plan -> plan.getId().equals(planId))
            .findFirst()
            .orElseThrow(() -> new Exception("Plan not found or does not belong to user"));
        
        // Actualizar los campos
        existingPlan.setTitle(title);
        existingPlan.setSummary(summary);
        existingPlan.setUpdatedAt(LocalDateTime.now());
        
        return businessPlanRepository.update(existingPlan);
    }

    public void deleteBusinessPlan(String userId, String planId) throws Exception {
        // Primero verificar que el plan existe y pertenece al usuario
        List<BusinessPlan> userPlans = businessPlanRepository.findByUserId(userId);
        BusinessPlan existingPlan = userPlans.stream()
            .filter(plan -> plan.getId().equals(planId))
            .findFirst()
            .orElseThrow(() -> new Exception("Plan not found or does not belong to user"));
        
        businessPlanRepository.deleteById(planId);
    }
}