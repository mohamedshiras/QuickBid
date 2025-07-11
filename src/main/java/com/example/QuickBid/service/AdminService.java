package com.example.QuickBid.service;

import com.example.QuickBid.model.Admin;
import com.example.QuickBid.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Admin getByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        return adminRepository.findByAdminUsername(username.trim()).orElse(null);
    }

    public boolean authenticateAdmin(String username, String password) {
        System.out.println("DEBUG: Service authenticateAdmin called with username: " + username);

        // Input validation
        if (username == null || username.trim().isEmpty() ||
                password == null || password.isEmpty()) {
            System.out.println("DEBUG: Input validation failed");
            return false;
        }

        Admin admin = getByUsername(username.trim());
        if (admin == null) {
            System.out.println("DEBUG: Admin not found for username: " + username);
            return false;
        }

        System.out.println("DEBUG: Found admin - Password from DB: '" + admin.getAdminPassword() +
                "', Password provided: '" + password + "'");

        // Plain text password comparison for testing
        boolean passwordMatch = admin.getAdminPassword().equals(password);
        System.out.println("DEBUG: Password match: " + passwordMatch);

        return passwordMatch;
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