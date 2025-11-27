package com.example.QuickBid.service;

import com.example.QuickBid.model.Admin;
import com.example.QuickBid.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Admin authenticateAdmin(String username, String password) {
        Optional<Admin> adminOptional = adminRepository.findByAdminUsername(username);

        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            // Plain text password comparison
            if (Objects.equals(admin.getAdminPassword(), password)) {
                return admin;
            }
        }
        return null; // Return null if user not found or password doesn't match
    }

    public Admin getByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        return adminRepository.findByAdminUsername(username.trim()).orElse(null);
    }

    // Method to create new admin for testing
    public Admin createAdmin(String username, String password) {
        Admin admin = new Admin();
        admin.setAdminUsername(username);
        admin.setAdminPassword(password);
        admin.setCreatedAt(java.time.LocalDateTime.now());
        return adminRepository.save(admin);
    }

    // Method to check if admin exists
    public boolean adminExists(String username) {
        return getByUsername(username) != null;
    }
}