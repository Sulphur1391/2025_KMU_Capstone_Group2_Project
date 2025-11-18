package com.outfit.ai.cloth_app.dto.response;

import java.time.OffsetDateTime;

public class MyPageProfileResponseDto {
    private String userName;
    private String email;
    private String region;
    private String profileImageUrl;
    private OffsetDateTime createdAt;

    public MyPageProfileResponseDto(String userName, String email, String region, String profileImageUrl, OffsetDateTime createdAt) {
        this.userName = userName;
        this.email = email;
        this.region = region;
        this.profileImageUrl = profileImageUrl;
        this.createdAt = createdAt;
    }

    public String getUserName() { return userName; }
    public String getEmail() { return email; }
    public String getRegion() { return region; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    public void setUserName(String userName) { this.userName = userName; }
    public void setEmail(String email) { this.email = email; }
    public void setRegion(String region) { this.region = region; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
