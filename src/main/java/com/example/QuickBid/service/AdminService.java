package com.example.QuickBid.service;

import com.example.QuickBid.model.Admin;
import com.example.QuickBid.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Admin registerAdmin(String username, String password) {
        if (adminRepository.existsByAdminUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        Admin admin = new Admin();
        admin.setAdminUsername(username);
        admin.setAdminPassword(passwordEncoder.encode(password));
        return adminRepository.save(admin);
    }

    public Admin authenticateAdmin(String username, String password) {
        Admin admin = adminRepository.findByAdminUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!passwordEncoder.matches(password, admin.getAdminPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return admin;
    }
}