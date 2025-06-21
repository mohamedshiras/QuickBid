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

    public User registerUser(String fullname, String address, String contact,
                             String username, String email, String password, Long whoApproved) {
        try {
            // Validate input parameters
            if (fullname == null || fullname.trim().isEmpty()) {
                throw new RuntimeException("Full name cannot be empty!");
            }

            if (address == null || address.trim().isEmpty()) {
                throw new RuntimeException("Address cannot be empty!");
            }

            if (contact == null || contact.trim().isEmpty()) {
                throw new RuntimeException("Contact number cannot be empty!");
            }

            if (username == null || username.trim().isEmpty()) {
                throw new RuntimeException("Username cannot be empty!");
            }

            if (email == null || email.trim().isEmpty()) {
                throw new RuntimeException("Email cannot be empty!");
            }

            if (password == null || password.trim().isEmpty()) {
                throw new RuntimeException("Password cannot be empty!");
            }

            // Check username length and format
            if (username.length() < 3) {
                throw new RuntimeException("Username must be at least 3 characters long!");
            }

            if (username.length() > 50) {
                throw new RuntimeException("Username cannot exceed 50 characters!");
            }

            // Check if username contains only valid characters (letters, numbers, underscore)
            if (!username.matches("^[a-zA-Z0-9_]+$")) {
                throw new RuntimeException("Username can only contain letters, numbers, and underscores!");
            }

            // Check if username already exists
            if (userRepository.existsByUsername(username)) {
                throw new RuntimeException("Username '" + username + "' is already taken!");
            }

            // Check if email already exists
            if (userRepository.existsByEmail(email.toLowerCase())) {
                throw new RuntimeException("Email '" + email + "' is already registered!");
            }

            // Validate fullname length
            if (fullname.length() > 50) {
                throw new RuntimeException("Full name cannot exceed 50 characters!");
            }

            // Validate address length
            if (address.length() > 50) {
                throw new RuntimeException("Address cannot exceed 50 characters!");
            }

            // Validate contact number format
            if (!contact.matches("^[0-9]{10}$")) {
                throw new RuntimeException("Contact number must be exactly 10 digits!");
            }

            // Validate email format
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new RuntimeException("Please enter a valid email address!");
            }

            // Check if approving admin exists
            if (!adminRepository.existsById(whoApproved)) {
                throw new RuntimeException("Invalid approving admin!");
            }

            // Create new user
            User newUser = new User(
                    fullname.trim(),
                    address.trim(),
                    contact.trim(),
                    username.trim(),
                    email.trim().toLowerCase(),
                    password,
                    whoApproved
            );

            return userRepository.save(newUser);

        } catch (Exception e) {
            // If it's already a RuntimeException, re-throw it
            if (e instanceof RuntimeException) {
                throw e;
            }
            // Otherwise, wrap it
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