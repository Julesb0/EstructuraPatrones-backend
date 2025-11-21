package com.miapp.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "platform.tables")
public class PlatformTablesProperties {
    private String profiles;
    private String businessPlans;
    private String userColumn;

    public String getProfiles() {
        return profiles;
    }

    public void setProfiles(String profiles) {
        this.profiles = profiles;
    }

    public String getBusinessPlans() {
        return businessPlans;
    }

    public void setBusinessPlans(String businessPlans) {
        this.businessPlans = businessPlans;
    }

    public String getUserColumn() {
        return userColumn;
    }

    public void setUserColumn(String userColumn) {
        this.userColumn = userColumn;
    }
}