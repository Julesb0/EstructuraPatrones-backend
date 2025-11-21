package com.miapp.core.profile.web;

import com.miapp.core.profile.facade.ProfileFacade;
import com.miapp.auth.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for profile management
 * Handles profile-related HTTP requests
 */
@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3002"})
public class ProfileController {

    private final ProfileFacade profileFacade;
    private final JwtService jwtService;

    @Autowired
    public ProfileController(ProfileFacade profileFacade, JwtService jwtService) {
        this.profileFacade = profileFacade;
        this.jwtService = jwtService;
    }

    /**
     * Get current user's profile
     * GET /api/profile/me
     * @param request HTTP request with Authorization header
     * @return ProfileResponse or 404 if not found
     */
    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile(HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
            if (userId == null) {
                return ResponseEntity.status(401).build();
            }

            ProfileResponse profile = profileFacade.getCurrentUserProfile(userId);
            if (profile != null) {
                return ResponseEntity.ok(profile);
            } else {
                // Return empty profile structure for new users
                ProfileResponse emptyProfile = new ProfileResponse();
                emptyProfile.setUserId(userId);
                emptyProfile.setFullName("");
                emptyProfile.setRole("ENTREPRENEUR");
                emptyProfile.setCountry("");
                return ResponseEntity.ok(emptyProfile);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Update current user's profile
     * PUT /api/profile/me
     * @param request HTTP request with Authorization header
     * @param updateRequest Profile update data
     * @return Updated ProfileResponse or error
     */
    @PutMapping("/me")
    public ResponseEntity<ProfileResponse> updateMyProfile(
            HttpServletRequest request,
            @Valid @RequestBody ProfileUpdateRequest updateRequest) {
        try {
            String userId = extractUserIdFromRequest(request);
            if (userId == null) {
                return ResponseEntity.status(401).build();
            }

            ProfileResponse updatedProfile = profileFacade.updateProfile(userId, updateRequest);
            return ResponseEntity.ok(updatedProfile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Extract user ID from JWT token in Authorization header
     * @param request HTTP request
     * @return User ID or null if not found
     */
    private String extractUserIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        try {
            String token = authHeader.substring(7);
            
            // Validate token first
            if (!jwtService.validateToken(token) || jwtService.isTokenExpired(token)) {
                return null;
            }
            
            // Extract user ID from JWT
            return jwtService.extractUserId(token);
        } catch (Exception e) {
            return null;
        }
    }
}