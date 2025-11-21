package com.miapp.core.profile.service;

import com.miapp.core.profile.domain.UserProfile;
import com.miapp.core.profile.repository.ProfileRepository;
import com.miapp.core.profile.web.ProfileUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {
    
    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Optional<UserProfile> getCurrentUserProfile(UUID userId) {
        return profileRepository.findByUserId(userId);
    }

    public UserProfile updateProfile(UUID userId, ProfileUpdateRequest updateRequest) {
        Optional<UserProfile> existingProfile = profileRepository.findByUserId(userId);
        
        UserProfile profile;
        if (existingProfile.isPresent()) {
            // Actualizar perfil existente
            profile = existingProfile.get();
            profile.setFullName(updateRequest.getFullName());
            profile.setRole(updateRequest.getRole());
            profile.setCountry(updateRequest.getCountry());
            profile.setUpdatedAt(LocalDateTime.now());
            
            return profileRepository.update(profile);
        } else {
            // Crear nuevo perfil si no existe
            profile = new UserProfile(
                userId,
                updateRequest.getFullName(),
                updateRequest.getRole(),
                updateRequest.getCountry()
            );
            
            return profileRepository.save(profile);
        }
    }

    public boolean profileExists(UUID userId) {
        return profileRepository.existsByUserId(userId);
    }
}