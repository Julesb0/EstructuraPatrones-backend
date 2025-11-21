package com.miapp.core.repository;

import com.miapp.core.config.PlatformTablesProperties;
import com.miapp.core.domain.BusinessPlan;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BusinessPlanRepository {
    private final SupabaseRepository supabaseRepository;
    private final PlatformTablesProperties tablesProperties;

    public BusinessPlanRepository(SupabaseRepository supabaseRepository, PlatformTablesProperties tablesProperties) {
        this.supabaseRepository = supabaseRepository;
        this.tablesProperties = tablesProperties;
    }

    public List<BusinessPlan> findByUserId(String userId) throws Exception {
        return supabaseRepository.findAllByUserId(
            tablesProperties.getBusinessPlans(), 
            userId, 
            tablesProperties.getUserColumn(), 
            BusinessPlan.class
        );
    }

    public BusinessPlan save(BusinessPlan businessPlan) throws Exception {
        return supabaseRepository.save(tablesProperties.getBusinessPlans(), businessPlan, BusinessPlan.class);
    }
}