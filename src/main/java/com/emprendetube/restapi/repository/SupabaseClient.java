package com.emprendetube.restapi.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class SupabaseClient {

    private final Dotenv dotenv;
    private final ObjectMapper objectMapper;
    private Connection connection;

    public SupabaseClient() {
        this.dotenv = Dotenv.load();
        this.objectMapper = new ObjectMapper();
        initializeConnection();
    }

    private void initializeConnection() {
        try {
            String url = dotenv.get("SUPABASE_URL");
            String user = dotenv.get("SUPABASE_USER");
            String password = dotenv.get("SUPABASE_PASSWORD");

            if (url == null || user == null || password == null) {
                throw new RuntimeException("Missing Supabase configuration in environment variables");
            }

            // Convertir URL de Supabase a formato JDBC PostgreSQL
            String jdbcUrl = url.replace("https://", "jdbc:postgresql://")
                    .replace(".supabase.co", ".supabase.co:5432") + "/postgres";

            this.connection = DriverManager.getConnection(jdbcUrl, user, password);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing Supabase connection: " + e.getMessage(), e);
        }
    }

    public void execute(String sql, Object... params) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            stmt.executeUpdate();
        }
    }

    public <T> List<T> query(String sql, Class<T> clazz, Object... params) throws SQLException {
        List<T> results = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    T entity = mapResultSetToEntity(rs, clazz);
                    results.add(entity);
                }
            }
        }
        
        return results;
    }

    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param == null) {
                stmt.setNull(i + 1, Types.NULL);
            } else if (param instanceof String) {
                stmt.setString(i + 1, (String) param);
            } else if (param instanceof Integer) {
                stmt.setInt(i + 1, (Integer) param);
            } else if (param instanceof Long) {
                stmt.setLong(i + 1, (Long) param);
            } else if (param instanceof java.time.LocalDateTime) {
                stmt.setTimestamp(i + 1, Timestamp.valueOf((java.time.LocalDateTime) param));
            } else if (param instanceof java.util.UUID) {
                stmt.setObject(i + 1, param);
            } else {
                stmt.setObject(i + 1, param);
            }
        }
    }

    private <T> T mapResultSetToEntity(ResultSet rs, Class<T> clazz) throws SQLException {
        try {
            T entity = clazz.getDeclaredConstructor().newInstance();
            
            // Mapear campos comunes
            try {
                entity.getClass().getMethod("setId", java.util.UUID.class).invoke(entity, rs.getObject("id", java.util.UUID.class));
            } catch (Exception e) {
                // El campo id puede no existir en todas las entidades
            }
            
            try {
                entity.getClass().getMethod("setUserId", java.util.UUID.class).invoke(entity, rs.getObject("user_id", java.util.UUID.class));
            } catch (Exception e) {
                // El campo user_id puede no existir en todas las entidades
            }
            
            try {
                entity.getClass().getMethod("setContent", String.class).invoke(entity, rs.getString("content"));
            } catch (Exception e) {
                // El campo content puede no existir en todas las entidades
            }
            
            try {
                entity.getClass().getMethod("setCreatedAt", java.time.LocalDateTime.class).invoke(entity, rs.getTimestamp("created_at").toLocalDateTime());
            } catch (Exception e) {
                // El campo created_at puede no existir en todas las entidades
            }
            
            // Mapear campos específicos de ChatMessage
            if (clazz.getSimpleName().equals("ChatMessage")) {
                try {
                    String role = rs.getString("role");
                    if (role != null) {
                        entity.getClass().getMethod("setRole", com.emprendetube.restapi.entity.ChatMessageRole.class)
                                .invoke(entity, com.emprendetube.restapi.entity.ChatMessageRole.valueOf(role));
                    }
                    
                    String category = rs.getString("category");
                    if (category != null) {
                        entity.getClass().getMethod("setCategory", com.emprendetube.restapi.entity.ChatMessageCategory.class)
                                .invoke(entity, com.emprendetube.restapi.entity.ChatMessageCategory.valueOf(category));
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error mapping ChatMessage fields: " + e.getMessage(), e);
                }
            }
            
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping ResultSet to entity: " + e.getMessage(), e);
        }
    }
}