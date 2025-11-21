package com.miapp.core.entity;

import java.util.Date;
import java.util.List;

public class Profile {
    private String id;
    private String email;
    private String username;
    private String fullName;
    private String bio;
    private String companyName;
    private String industry;
    private String website;
    private String linkedinUrl;
    private String location;
    private String avatarUrl;
    private String phone;
    private Date birthDate;
    private String experienceLevel;
    private List<String> skills;
    private List<String> interests;
    private Boolean isMentor;
    private Boolean isInvestor;
    private Integer yearsExperience;
    private String companyStage;
    private String fundingStage;
    private Integer teamSize;
    private String revenueRange;
    private List<String> lookingFor;
    private Date createdAt;
    private Date updatedAt;

    public Profile() {}

    public Profile(String id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }

    public String getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(String experienceLevel) { this.experienceLevel = experienceLevel; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    public List<String> getInterests() { return interests; }
    public void setInterests(List<String> interests) { this.interests = interests; }

    public Boolean getIsMentor() { return isMentor; }
    public void setIsMentor(Boolean mentor) { isMentor = mentor; }

    public Boolean getIsInvestor() { return isInvestor; }
    public void setIsInvestor(Boolean investor) { isInvestor = investor; }

    public Integer getYearsExperience() { return yearsExperience; }
    public void setYearsExperience(Integer yearsExperience) { this.yearsExperience = yearsExperience; }

    public String getCompanyStage() { return companyStage; }
    public void setCompanyStage(String companyStage) { this.companyStage = companyStage; }

    public String getFundingStage() { return fundingStage; }
    public void setFundingStage(String fundingStage) { this.fundingStage = fundingStage; }

    public Integer getTeamSize() { return teamSize; }
    public void setTeamSize(Integer teamSize) { this.teamSize = teamSize; }

    public String getRevenueRange() { return revenueRange; }
    public void setRevenueRange(String revenueRange) { this.revenueRange = revenueRange; }

    public List<String> getLookingFor() { return lookingFor; }
    public void setLookingFor(List<String> lookingFor) { this.lookingFor = lookingFor; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}