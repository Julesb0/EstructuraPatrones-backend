package com.miapp.core.domain;

import java.time.LocalDateTime;

/**
 * Entidad BusinessPlan con todos los campos necesarios para el CRUD
 * Incluye status para tracking de estado del plan de negocio
 * Última actualización: FORZAR REBUILD EN RENDER - VERSIÓN 2.2
 * Fecha: 2025-11-21 19:30 - Cambio forzado para redeploy
 */
public class BusinessPlan {
    private String id;
    private String userId;
    private String title;
    private String summary;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BusinessPlan() {}

    public BusinessPlan(String id, String userId, String title, String summary, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.summary = summary;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}