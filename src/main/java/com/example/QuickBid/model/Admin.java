package com.example.QuickBid.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminID;

    @Column(nullable = false, unique = true, length = 50)
    private String adminUsername;

    @Column(nullable = false)
    private String adminPassword;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    // Constructors
    public Admin() {
        this.createdAt = LocalDateTime.now();
    }

    public Admin(String adminUsername, String adminPassword) {
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getAdminID() { return adminID; }
    public void setAdminID(Long adminID) { this.adminID = adminID; }

    public String getAdminUsername() { return adminUsername; }
    public void setAdminUsername(String adminUsername) { this.adminUsername = adminUsername; }

    public String getAdminPassword() { return adminPassword; }
    public void setAdminPassword(String adminPassword) { this.adminPassword = adminPassword; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}