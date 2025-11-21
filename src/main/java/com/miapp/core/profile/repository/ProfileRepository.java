package com.miapp.core.profile.repository;

import com.miapp.core.profile.domain.UserProfile;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository {
    Optional<UserProfile> findByUserId(UUID userId);
    UserProfile save(UserProfile profile);
    UserProfile update(UserProfile profile);
    boolean existsByUserId(UUID userId);
}