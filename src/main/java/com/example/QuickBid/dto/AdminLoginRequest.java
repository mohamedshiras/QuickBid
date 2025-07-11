package com.example.QuickBid.dto;

public class AdminLoginRequest {
    private String adminUsername;
    private String adminPassword;

    // Getters and Setters
    public String getAdminUsername() { return adminUsername; }
    public void setAdminUsername(String adminUsername) { this.adminUsername = adminUsername; }
    public String getAdminPassword() { return adminPassword; }
    public void setAdminPassword(String adminPassword) { this.adminPassword = adminPassword; }
}