package com.example.QuickBid.service;

import com.example.QuickBid.model.User;
import com.example.QuickBid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get user by username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Update user profile
    public User updateUser(Long userId, String fullname, String address, String contact, String email) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (fullname != null && !fullname.trim().isEmpty()) {
                user.setFullname(fullname.trim());
            }

            if (address != null && !address.trim().isEmpty()) {
                user.setAddress(address.trim());
            }

            if (contact != null && !contact.trim().isEmpty() && contact.matches("\\d{10}")) {
                user.setContact(contact.trim());
            }

            if (email != null && !email.trim().isEmpty() && email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                // Check if email is already taken by another user
                Optional<User> existingUser = userRepository.findByEmail(email);
                if (existingUser.isEmpty() || existingUser.get().getId().equals(userId)) {
                    user.setEmail(email.trim());
                }
            }

            return userRepository.save(user);
        }

        throw new RuntimeException("User not found with ID: " + userId);
    }

    // Change password
    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Verify current password
            if (passwordEncoder.matches(currentPassword, user.getPassword())) {
                // Hash and set new password
                String hashedNewPassword = passwordEncoder.encode(newPassword);
                user.setPassword(hashedNewPassword);
                userRepository.save(user);
                return true;
            }
        }

        return false;
    }

    // Delete user
    public boolean deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Check if username exists
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // Check if email exists
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Get user count
    public long getUserCount() {
        return userRepository.count();
    }

    // Search users by name or username
    public List<User> searchUsers(String searchTerm) {
        // This would require a custom query method in the repository
        // For now, we'll return all users and filter in the service
        return userRepository.findAll().stream()
                .filter(user ->
                        user.getFullname().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                user.getUsername().toLowerCase().contains(searchTerm.toLowerCase())
                )
                .toList();
    }
}