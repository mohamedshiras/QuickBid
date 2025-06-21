package com.example.QuickBid.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adminID")
    private Long adminId;

    @Column(name = "adminUsername", nullable = false, unique = true, length = 50)
    private String adminUsername;

    @Column(name = "adminPassword", nullable = false, length = 255)
    private String adminPassword;

    @Column(name = "createdAt", nullable = false)
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
    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}