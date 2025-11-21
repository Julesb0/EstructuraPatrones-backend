package com.miapp.core.profile.facade;

import com.miapp.core.profile.domain.UserProfile;
import com.miapp.core.profile.service.ProfileService;
import com.miapp.core.profile.web.ProfileResponse;
import com.miapp.core.profile.web.ProfileUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ProfileFacade {
    
    private final ProfileService profileService;

    @Autowired
    public ProfileFacade(ProfileService profileService) {
        this.profileService = profileService;
    }

    public Optional<ProfileResponse> getCurrentUserProfile(UUID userId) {
        // Validaciones de negocio
        if (userId == null) {
            throw new IllegalArgumentException("El ID de usuario no puede ser nulo");
        }

        Optional<UserProfile> profile = profileService.getCurrentUserProfile(userId);
        
        if (profile.isPresent()) {
            return Optional.of(new ProfileResponse(profile.get()));
        }
        
        return Optional.empty();
    }

    public ProfileResponse updateProfile(UUID userId, ProfileUpdateRequest updateRequest) {
        // Validaciones de negocio
        if (userId == null) {
            throw new IllegalArgumentException("El ID de usuario no puede ser nulo");
        }
        
        if (updateRequest == null) {
            throw new IllegalArgumentException("Los datos de actualización no pueden ser nulos");
        }

        // Validar rol
        validateRole(updateRequest.getRole());
        
        // Actualizar perfil a través del servicio
        UserProfile updatedProfile = profileService.updateProfile(userId, updateRequest);
        
        // Convertir a respuesta
        return new ProfileResponse(updatedProfile);
    }

    private void validateRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("El rol no puede estar vacío");
        }
        
        // Validar que el rol sea uno de los permitidos
        String[] validRoles = {"ENTREPRENEUR", "MENTOR", "INVESTOR", "ADMIN"};
        boolean isValidRole = false;
        
        for (String validRole : validRoles) {
            if (validRole.equals(role.toUpperCase())) {
                isValidRole = true;
                break;
            }
        }
        
        if (!isValidRole) {
            throw new IllegalArgumentException("Rol inválido. Los roles permitidos son: ENTREPRENEUR, MENTOR, INVESTOR, ADMIN");
        }
    }

    public boolean profileExists(UUID userId) {
        if (userId == null) {
            return false;
        }
        return profileService.profileExists(userId);
    }
}