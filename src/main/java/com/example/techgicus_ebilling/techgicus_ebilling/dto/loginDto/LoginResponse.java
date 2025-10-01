package com.example.techgicus_ebilling.techgicus_ebilling.dto.loginDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema
public class LoginResponse {

    @Schema(example = "1")
    private Long userId;

    @Schema(example = "Admin")
    private String userName;

    @Schema(example = "payalpaunikar1001@gmail.com")
    private String emailId;

    @Schema(example = "[SUPERADMIN]")
    private Set roleName;

    @Schema(example = "true")
    private Boolean isSubscriptionActive;

    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXlhbHBhdW5pa2FyMTAwMUBnbWFpbC5jb20iLCJyb2xlIjpbIlNVUEVSQURNSU4iXSwiaWF0IjoxNzU2NDUzNjM0LCJleHAiOjE3NTY1NDAwMzR9.euPEzeNCRG4CXxaSoYBWDHSDRAVI7F0q8mCe1s5poDM")
    private String accessToken;

    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXlhbHBhdW5pa2FyMTAwMUBnbWFpbC5jb20iLCJ0eXBlIjoicmVmcmVzaCIsImlhdCI6MTc1NjQ1MzYzNSwiZXhwIjoxNzU3MDU4NDM1fQ.XezGE0rwIY5NIB9NZmanvYULiuO7Rtw9rXp_UwtpOFY")
    private String refreshToken;

    public LoginResponse() {
    }

    public LoginResponse(Long userId, String userName, String emailId, Set roleName, Boolean isSubscriptionActive, String accessToken, String refreshToken) {
        this.userId = userId;
        this.userName = userName;
        this.emailId = emailId;
        this.roleName = roleName;
        this.isSubscriptionActive = isSubscriptionActive;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Set getRoleName() {
        return roleName;
    }

    public void setRoleName(Set roleName) {
        this.roleName = roleName;
    }

    public Boolean getSubscriptionActive() {
        return isSubscriptionActive;
    }

    public void setSubscriptionActive(Boolean subscriptionActive) {
        isSubscriptionActive = subscriptionActive;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
