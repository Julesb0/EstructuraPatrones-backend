package com.miapp.core.profile.facade;

import com.miapp.core.profile.domain.UserProfile;
import com.miapp.core.profile.service.ProfileService;
import com.miapp.core.profile.web.ProfileResponse;
import com.miapp.core.profile.web.ProfileUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ProfileFacade following the Facade pattern
 * Orchestrates profile-related operations and validations
 */
@Component
public class ProfileFacade {

    private final ProfileService profileService;

    @Autowired
    public ProfileFacade(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * Get current user's profile with validation
     * @param userId the user ID from JWT
     * @return ProfileResponse or null if not found
     */
    public ProfileResponse getCurrentUserProfile(String userId) {
        validateUserId(userId);
        
        UserProfile profile = profileService.getCurrentUserProfile(userId);
        if (profile == null) {
            return null;
        }
        
        return convertToResponse(profile);
    }

    /**
     * Update current user's profile with validation
     * @param userId the user ID from JWT
     * @param updateRequest the update request
     * @return updated ProfileResponse
     * @throws IllegalArgumentException if validation fails
     */
    public ProfileResponse updateProfile(String userId, ProfileUpdateRequest updateRequest) {
        validateUserId(userId);
        validateUpdateRequest(updateRequest);
        
        UserProfile updatedProfile = profileService.updateProfile(userId, updateRequest);
        if (updatedProfile == null) {
            throw new RuntimeException("Failed to update profile");
        }
        
        return convertToResponse(updatedProfile);
    }

    /**
     * Create a new profile for a user
     * @param userId the user ID
     * @param fullName the full name
     * @param role the role
     * @param country the country
     * @return created ProfileResponse
     */
    public ProfileResponse createProfile(String userId, String fullName, String role, String country) {
        validateUserId(userId);
        
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role is required");
        }
        if (!profileService.isValidRole(role)) {
            throw new IllegalArgumentException("Invalid role. Must be one of: ENTREPRENEUR, MENTOR, INVESTOR, ADMIN");
        }
        
        UserProfile profile = profileService.createProfile(userId, fullName, role, country);
        return convertToResponse(profile);
    }

    /**
     * Check if user has a specific role
     * @param userId the user ID
     * @param role the role to check
     * @return true if user has the role
     */
    public boolean hasRole(String userId, String role) {
        validateUserId(userId);
        return profileService.hasRole(userId, role);
    }

    private void validateUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
    }

    private void validateUpdateRequest(ProfileUpdateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Update request is required");
        }
        
        if (request.getRole() != null && !profileService.isValidRole(request.getRole())) {
            throw new IllegalArgumentException("Invalid role. Must be one of: ENTREPRENEUR, MENTOR, INVESTOR, ADMIN");
        }
    }

    private ProfileResponse convertToResponse(UserProfile profile) {
        ProfileResponse response = new ProfileResponse();
        response.setUserId(profile.getUserId());
        response.setFullName(profile.getFullName());
        response.setRole(profile.getRole());
        response.setCountry(profile.getCountry());
        response.setCreatedAt(profile.getCreatedAt());
        response.setUpdatedAt(profile.getUpdatedAt());
        return response;
    }
}