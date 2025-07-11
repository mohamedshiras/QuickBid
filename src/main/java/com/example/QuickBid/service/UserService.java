package com.example.QuickBid.service;

import com.example.QuickBid.model.User;
import com.example.QuickBid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get users by status (PENDING, ACTIVE, SUSPENDED, etc.)
    public List<User> getUsersByStatus(String status) {
        return userRepository.findByAccountStatus(status);
    }

    // Get pending confirmation users
    public List<User> getPendingConfirmationUsers() {
        return userRepository.findByAccountStatus("PENDING");
    }

    // Approve a pending user
    public void approveUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (!"PENDING".equals(user.getAccountStatus())) {
            throw new RuntimeException("User is not in PENDING status");
        }

        user.setAccountStatus("ACTIVE");
        userRepository.save(user);
    }

    // Reject a pending user
    public void rejectUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (!"PENDING".equals(user.getAccountStatus())) {
            throw new RuntimeException("User is not in PENDING status");
        }

        user.setAccountStatus("REJECTED");
        userRepository.save(user);
    }

    // Update user status (generic method)
    public void updateUserStatus(Integer userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setAccountStatus(status);
        userRepository.save(user);
    }

    public void DeleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }
}