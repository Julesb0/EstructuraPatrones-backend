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
        plan.setCreatedAt(LocalDateTime.now());
        plan.setUpdatedAt(LocalDateTime.now());
        
        return businessPlanRepository.save(plan);
    }
}