package com.example.QuickBid.service;
import com.example.QuickBid.model.User;
import com.example.QuickBid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public LoginResult authenticateUser(String usernameOrEmail, String password) {
        try {
            // Find user by username or email
            Optional<User> userOptional = userRepository.findByUsernameOrEmail(usernameOrEmail);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                if ("pending".equals(user.getAccountStatus())) {
                    return new LoginResult(false, "Account pending confirmation", null, null);
                }

                // Trim passwords to avoid whitespace issues
                if (user.getPassword().trim().equals(password.trim())) {
                    return new LoginResult(true, "Login successful", user, "USER");
                } else {
                    return new LoginResult(false, "Invalid credentials", null, null);
                }
            }

            return new LoginResult(false, "User not found", null, null);

        } catch (Exception e) {
            return new LoginResult(false, "Authentication failed: " + e.getMessage(), null, null);
        }
    }

    public User registerUser(String fullname, String address, String contact,
                             String username, String email, String password) {
        try {
            // Check for existing user
            if (userRepository.existsByUsername(username)) {
                throw new RuntimeException("Username already taken");
            }
            if (userRepository.existsByEmail(email)) {
                throw new RuntimeException("Email already registered");
            }

            // Create new user
            User newUser = new User();
            newUser.setFullname(fullname);
            newUser.setAddress(address);
            newUser.setContact(contact);
            newUser.setUsername(username);
            newUser.setEmail(email.toLowerCase());
            newUser.setPassword(password);
            newUser.setAccountStatus("pending confirmation");
            newUser.setCreatedAt(LocalDateTime.now());

            return userRepository.save(newUser);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    public static class LoginResult {
        private final boolean success;
        private final String message;
        private final User user;
        private final String userType;

        public LoginResult(boolean success, String message, User user, String userType) {
            this.success = success;
            this.message = message;
            this.user = user;
            this.userType = userType;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public User getUser() { return user; }
        public String getUserType() { return userType; }
    }
}