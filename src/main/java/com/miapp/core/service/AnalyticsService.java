package com.miapp.core.service;

import com.miapp.core.domain.BusinessPlan;
import com.miapp.core.repository.BusinessPlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class AnalyticsService {
    
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsService.class);
    
    private final BusinessPlanRepository businessPlanRepository;
    
    public AnalyticsService(BusinessPlanRepository businessPlanRepository) {
        this.businessPlanRepository = businessPlanRepository;
    }
    
    /**
     * Calcula métricas de análisis para un usuario específico
     * 
     * @param userId ID del usuario
     * @return Map con las métricas calculadas
     */
    public Map<String, Object> calculateUserAnalytics(String userId) {
        try {
            List<BusinessPlan> userPlans = businessPlanRepository.findByUserId(userId);
            
            int totalPlans = userPlans.size();
            int completedPlans = (int) userPlans.stream()
                .filter(plan -> "completed".equalsIgnoreCase(plan.getStatus()))
                .count();
            int inProgressPlans = (int) userPlans.stream()
                .filter(plan -> "in_progress".equalsIgnoreCase(plan.getStatus()))
                .count();
            
            // Calcular porcentaje de éxito
            double successRate = totalPlans > 0 ? (completedPlans * 100.0) / totalPlans : 0.0;
            
            // Ingresos estimados (valor fijo por plan completado para simplificar)
            double estimatedRevenue = completedPlans * 5000.0; // $5,000 por plan completado
            
            Map<String, Object> analytics = new HashMap<>();
            analytics.put("totalPlans", totalPlans);
            analytics.put("completedPlans", completedPlans);
            analytics.put("inProgressPlans", inProgressPlans);
            analytics.put("successRate", Math.round(successRate * 100.0) / 100.0); // Redondear a 2 decimales
            analytics.put("estimatedRevenue", Math.round(estimatedRevenue * 100.0) / 100.0);
            
            logger.info("Analytics calculated for user {}: total={}, completed={}, inProgress={}, successRate={}%", 
                       userId, totalPlans, completedPlans, inProgressPlans, successRate);
            
            return analytics;
            
        } catch (Exception e) {
            logger.error("Error calculating analytics for user {}: {}", userId, e.getMessage(), e);
            
            // Retornar valores por defecto en caso de error
            Map<String, Object> errorAnalytics = new HashMap<>();
            errorAnalytics.put("totalPlans", 0);
            errorAnalytics.put("completedPlans", 0);
            errorAnalytics.put("inProgressPlans", 0);
            errorAnalytics.put("successRate", 0.0);
            errorAnalytics.put("estimatedRevenue", 0.0);
            errorAnalytics.put("error", "Error al calcular análisis");
            
            return errorAnalytics;
        }
    }
}