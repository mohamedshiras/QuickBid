package com.example.QuickBid.service;

import com.example.QuickBid.model.Admin;
import com.example.QuickBid.model.User;
import com.example.QuickBid.repository.AdminRepository;
import com.example.QuickBid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    public LoginResult authenticateUser(String usernameOrEmail, String password) {
        try {
            // First, try to find the user by username or email
            Optional<User> userOptional = userRepository.findByUsernameOrEmail(usernameOrEmail);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Check if password matches (plain text comparison)
                if (user.getPassword().equals(password)) {
                    return new LoginResult(true, "Login successful!", user, null, "USER");
                } else {
                    return new LoginResult(false, "Invalid password!", null, null, null);
                }
            }

            // If no user found, check admin credentials
            Optional<Admin> adminOptional = adminRepository.findByAdminUsername(usernameOrEmail);

            if (adminOptional.isPresent()) {
                Admin admin = adminOptional.get();

                // Check if password matches (plain text comparison)
                if (admin.getAdminPassword().equals(password)) {
                    return new LoginResult(true, "Admin login successful!", null, admin, "ADMIN");
                } else {
                    return new LoginResult(false, "Invalid admin password!", null, null, null);
                }
            }

            return new LoginResult(false, "User not found!", null, null, null);

        } catch (Exception e) {
            return new LoginResult(false, "Login failed: " + e.getMessage(), null, null, null);
        }
    }

    // AuthService.java

    // Modify the method signature to remove whoApproved
    public User registerUser(String fullname, String address, String contact,
                             String username, String email, String password) {
        try {

            // Create new user
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password); // Note: You should be hashing passwords!
            newUser.setFullname(fullname);
            newUser.setAddress(address);
            newUser.setContact(contact);
            newUser.setEmail(email);

            return userRepository.save(newUser);

        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    // Inner class to hold login result
    public static class LoginResult {
        private boolean success;
        private String message;
        private User user;
        private Admin admin;
        private String userType;

        public LoginResult(boolean success, String message, User user, Admin admin, String userType) {
            this.success = success;
            this.message = message;
            this.user = user;
            this.admin = admin;
            this.userType = userType;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public User getUser() { return user; }
        public Admin getAdmin() { return admin; }
        public String getUserType() { return userType; }
    }
}