package com.example.QuickBid.service;

import com.example.QuickBid.model.User;
import com.example.QuickBid.model.Admin;
import com.example.QuickBid.repository.UserRepository;
import com.example.QuickBid.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // User Login
    public Map<String, Object> loginUser(String usernameOrEmail, String password) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Find user by username or email
            Optional<User> userOptional = userRepository.findByUsernameOrEmail(usernameOrEmail);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Verify password
                if (passwordEncoder.matches(password, user.getPassword())) {
                    response.put("success", true);
                    response.put("message", "Login successful");
                    response.put("userId", user.getId());
                    response.put("username", user.getUsername());
                    response.put("email", user.getEmail());
                    response.put("fullname", user.getFullname());
                } else {
                    response.put("success", false);
                    response.put("message", "Invalid password");
                }
            } else {
                response.put("success", false);
                response.put("message", "User not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
        }

        return response;
    }

    // Admin Login
    public Map<String, Object> loginAdmin(String adminUsername, String password) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Find admin by username
            Optional<Admin> adminOptional = adminRepository.findByAdminUsername(adminUsername);

            if (adminOptional.isPresent()) {
                Admin admin = adminOptional.get();

                // Verify password
                if (passwordEncoder.matches(password, admin.getAdminPassword())) {
                    response.put("success", true);
                    response.put("message", "Admin login successful");
                    response.put("adminId", admin.getAdminId());
                    response.put("adminUsername", admin.getAdminUsername());
                } else {
                    response.put("success", false);
                    response.put("message", "Invalid password");
                }
            } else {
                response.put("success", false);
                response.put("message", "Admin not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Admin login failed: " + e.getMessage());
        }

        return response;
    }

    // User Registration
    public Map<String, Object> registerUser(String fullname, String address, String contact,
                                            String username, String email, String password,
                                            String confirmPassword) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validate input
            if (fullname == null || fullname.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Full name is required");
                return response;
            }

            if (address == null || address.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Address is required");
                return response;
            }

            if (contact == null || contact.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Contact number is required");
                return response;
            }

            if (username == null || username.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Username is required");
                return response;
            }

            if (email == null || email.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Email is required");
                return response;
            }

            if (password == null || password.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Password is required");
                return response;
            }

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                response.put("success", false);
                response.put("message", "Passwords do not match");
                return response;
            }

            // Check if username already exists
            if (userRepository.existsByUsername(username)) {
                response.put("success", false);
                response.put("message", "Username already exists");
                return response;
            }

            // Check if email already exists
            if (userRepository.existsByEmail(email)) {
                response.put("success", false);
                response.put("message", "Email already exists");
                return response;
            }

            // Validate contact number (should be 10 digits)
            if (!contact.matches("\\d{10}")) {
                response.put("success", false);
                response.put("message", "Contact number must be 10 digits");
                return response;
            }

            // Validate email format
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                response.put("success", false);
                response.put("message", "Invalid email format");
                return response;
            }

            // Get default admin ID (assuming admin with ID 1 exists for approvals)
            // You might want to modify this logic based on your requirements
            Long defaultAdminId = 1L;

            // Hash password
            String hashedPassword = passwordEncoder.encode(password);

            // Create new user
            User newUser = new User(fullname.trim(), address.trim(), contact.trim(),
                    username.trim(), email.trim(), hashedPassword, defaultAdminId);

            // Save user
            User savedUser = userRepository.save(newUser);

            response.put("success", true);
            response.put("message", "User registered successfully");
            response.put("userId", savedUser.getId());
            response.put("username", savedUser.getUsername());

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());
        }

        return response;
    }

    // Check if user exists
    public boolean userExists(String usernameOrEmail) {
        return userRepository.existsByUsernameOrEmail(usernameOrEmail);
    }

    // Get user by username or email
    public Optional<User> getUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail);
    }
}