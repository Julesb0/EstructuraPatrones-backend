package com.miapp.core.profile.service;

import com.miapp.core.profile.domain.UserProfile;
import com.miapp.core.profile.repository.ProfileRepository;
import com.miapp.core.profile.web.ProfileUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for profile management following single responsibility principle
 */
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    /**
     * Get the current user's profile
     * @param userId the user ID from JWT
     * @return UserProfile or null if not found
     */
    public UserProfile getCurrentUserProfile(String userId) {
        Optional<UserProfile> profile = profileRepository.findByUserId(userId);
        return profile.orElse(null);
    }

    /**
     * Update the current user's profile
     * @param userId the user ID from JWT
     * @param updateRequest the update request with new data
     * @return updated UserProfile or null if not found
     */
    public UserProfile updateProfile(String userId, ProfileUpdateRequest updateRequest) {
        Optional<UserProfile> existingProfile = profileRepository.findByUserId(userId);
        
        if (existingProfile.isEmpty()) {
            // Create new profile if it doesn't exist
            return profileRepository.createProfile(
                userId,
                updateRequest.getFullName(),
                updateRequest.getRole(),
                updateRequest.getCountry()
            );
        }

        UserProfile profile = existingProfile.get();
        
        // Update fields if provided
        if (updateRequest.getFullName() != null) {
            profile.setFullName(updateRequest.getFullName());
        }
        if (updateRequest.getRole() != null) {
            profile.setRole(updateRequest.getRole());
        }
        if (updateRequest.getCountry() != null) {
            profile.setCountry(updateRequest.getCountry());
        }

        return profileRepository.save(profile);
    }

    /**
     * Check if user has a specific role
     * @param userId the user ID
     * @param role the role to check
     * @return true if user has the role
     */
    public boolean hasRole(String userId, String role) {
        UserProfile profile = getCurrentUserProfile(userId);
        return profile != null && role.equals(profile.getRole());
    }

    /**
     * Create a new profile for a user
     * @param userId the user ID
     * @param fullName the full name
     * @param role the role
     * @param country the country
     * @return the created UserProfile
     */
    public UserProfile createProfile(String userId, String fullName, String role, String country) {
        return profileRepository.createProfile(userId, fullName, role, country);
    }

    /**
     * Validate if a role is valid
     * @param role the role to validate
     * @return true if valid
     */
    public boolean isValidRole(String role) {
        return role != null && (
            "ENTREPRENEUR".equals(role) ||
            "MENTOR".equals(role) ||
            "INVESTOR".equals(role) ||
            "ADMIN".equals(role)
        );
    }
}