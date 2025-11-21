package com.miapp.core.service;

import com.miapp.core.dto.ProfileDTO;
import com.miapp.core.entity.Profile;
import com.miapp.core.repository.SupabaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private SupabaseRepository supabaseRepository;

    @InjectMocks
    private ProfileService profileService;

    private Profile testProfile;
    private ProfileDTO testProfileDTO;
    private final String testUserId = "test-user-id-123";

    @BeforeEach
    void setUp() {
        // Setup test data
        testProfile = new Profile();
        testProfile.setId("profile-123");
        testProfile.setUserId(testUserId);
        testProfile.setEmail("test@example.com");
        testProfile.setUsername("testuser");
        testProfile.setFullName("Test User");
        testProfile.setBio("Test bio");
        testProfile.setCompanyName("Test Company");
        testProfile.setIndustry("Technology");
        testProfile.setSkills(Arrays.asList("Java", "Spring Boot", "React"));
        testProfile.setIsMentor(true);
        testProfile.setIsInvestor(false);

        testProfileDTO = new ProfileDTO();
        testProfileDTO.setId("profile-123");
        testProfileDTO.setEmail("test@example.com");
        testProfileDTO.setUsername("testuser");
        testProfileDTO.setFullName("Test User");
        testProfileDTO.setBio("Test bio");
        testProfileDTO.setCompanyName("Test Company");
        testProfileDTO.setIndustry("Technology");
        testProfileDTO.setSkills(Arrays.asList("Java", "Spring Boot", "React"));
        testProfileDTO.setIsMentor(true);
        testProfileDTO.setIsInvestor(false);
    }

    @Test
    void testGetProfile_Success() {
        when(supabaseRepository.findById(anyString(), anyString(), any())).n
            .thenReturn(testProfile);

        ProfileDTO result = profileService.getProfile(testUserId);

        assertNotNull(result);
        assertEquals(testProfileDTO.getEmail(), result.getEmail());
        assertEquals(testProfileDTO.getUsername(), result.getUsername());
        assertEquals(testProfileDTO.getFullName(), result.getFullName());
        
        verify(supabaseRepository).findById(eq("profiles"), eq(testUserId), eq(Profile.class));
    }

    @Test
    void testGetProfile_NotFound() {
        when(supabaseRepository.findById(anyString(), anyString(), any()))
            .thenReturn(null);

        ProfileDTO result = profileService.getProfile(testUserId);

        assertNull(result);
        verify(supabaseRepository).findById(eq("profiles"), eq(testUserId), eq(Profile.class));
    }

    @Test
    void testUpdateProfile_Success() {
        when(supabaseRepository.findById(anyString(), anyString(), any()))
            .thenReturn(testProfile);
        when(supabaseRepository.update(anyString(), anyString(), any()))
            .thenReturn(testProfile);

        ProfileDTO result = profileService.updateProfile(testUserId, testProfileDTO);

        assertNotNull(result);
        assertEquals(testProfileDTO.getEmail(), result.getEmail());
        assertEquals(testProfileDTO.getUsername(), result.getUsername());
        
        verify(supabaseRepository).findById(eq("profiles"), eq(testUserId), eq(Profile.class));
        verify(supabaseRepository).update(eq("profiles"), eq(testUserId), any(Profile.class));
    }

    @Test
    void testUpdateProfile_NotFound() {
        when(supabaseRepository.findById(anyString(), anyString(), any()))
            .thenReturn(null);

        ProfileDTO result = profileService.updateProfile(testUserId, testProfileDTO);

        assertNull(result);
        verify(supabaseRepository).findById(eq("profiles"), eq(testUserId), eq(Profile.class));
        verify(supabaseRepository, never()).update(anyString(), anyString(), any());
    }

    @Test
    void testConvertToDTO() {
        ProfileDTO result = profileService.convertToDTO(testProfile);

        assertNotNull(result);
        assertEquals(testProfile.getEmail(), result.getEmail());
        assertEquals(testProfile.getUsername(), result.getUsername());
        assertEquals(testProfile.getFullName(), result.getFullName());
        assertEquals(testProfile.getBio(), result.getBio());
        assertEquals(testProfile.getCompanyName(), result.getCompanyName());
        assertEquals(testProfile.getIndustry(), result.getIndustry());
        assertEquals(testProfile.getSkills(), result.getSkills());
        assertEquals(testProfile.getIsMentor(), result.getIsMentor());
        assertEquals(testProfile.getIsInvestor(), result.getIsInvestor());
    }
}