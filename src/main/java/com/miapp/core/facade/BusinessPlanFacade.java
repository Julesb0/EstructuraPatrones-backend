package com.miapp.core.facade;

import com.miapp.core.domain.BusinessPlan;
import com.miapp.core.service.BusinessPlanService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BusinessPlanFacade {
    private final BusinessPlanService businessPlanService;

    public BusinessPlanFacade(BusinessPlanService businessPlanService) {
        this.businessPlanService = businessPlanService;
    }

    public List<BusinessPlan> getUserBusinessPlans(String userId) throws Exception {
        return businessPlanService.getBusinessPlansByUserId(userId);
    }

    public BusinessPlan createBusinessPlan(String userId, String title, String summary) throws Exception {
        return businessPlanService.createBusinessPlan(userId, title, summary);
    }
}