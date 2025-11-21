package com.miapp.core.profile.repository;

import com.miapp.core.profile.domain.UserProfile;
import java.util.Optional;

/**
 * Repository interface for UserProfile following the Repository pattern
 */
public interface ProfileRepository {
    
    /**
     * Find a profile by user ID
     * @param userId the user ID
     * @return Optional containing the profile if found
     */
    Optional<UserProfile> findByUserId(String userId);
    
    /**
     * Save or update a profile
     * @param profile the profile to save
     * @return the saved profile
     */
    UserProfile save(UserProfile profile);
    
    /**
     * Check if a profile exists for a user ID
     * @param userId the user ID
     * @return true if profile exists
     */
    boolean existsByUserId(String userId);
    
    /**
     * Create a new profile for a user
     * @param userId the user ID
     * @param fullName the full name
     * @param role the role
     * @param country the country
     * @return the created profile
     */
    UserProfile createProfile(String userId, String fullName, String role, String country);
}