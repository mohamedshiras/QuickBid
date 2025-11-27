package com.example.QuickBid.service;

import com.example.QuickBid.model.User;
import com.example.QuickBid.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.password.PasswordEncoder; // REMOVED for plain text passwords
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // --- Methods for User Self-Management ---

    @Transactional
    public void updateProfilePicture(Integer userId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload an empty file.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        user.setProfilePicture(file.getBytes());
        userRepository.save(user);
    }

    @Transactional
    public void removeProfilePicture(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        user.setProfilePicture(null);
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(Integer userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Using plain text comparison. This is NOT secure.
        if (!Objects.equals(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect current password.");
        }

        // Using plain text comparison. This is NOT secure.
        if (Objects.equals(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as the old password.");
        }

        // Storing the new password in plain text. This is NOT secure.
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    // --- Methods for Administrative Management ---

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByStatus(String status) {
        return userRepository.findByAccountStatus(status);
    }

    public List<User> getPendingConfirmationUsers() {
        return userRepository.findByAccountStatus("PENDING");
    }

    @Transactional
    public void approveUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (!"PENDING".equals(user.getAccountStatus())) {
            throw new RuntimeException("User is not in PENDING status");
        }

        user.setAccountStatus("ACTIVE");
        userRepository.save(user);
    }

    @Transactional
    public void rejectUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (!"PENDING".equals(user.getAccountStatus())) {
            throw new RuntimeException("User is not in PENDING status");
        }

        user.setAccountStatus("REJECTED");
        userRepository.save(user);
    }

    @Transactional
    public void updateUserStatus(Integer userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setAccountStatus(status);
        userRepository.save(user);
    }

    @Transactional
    public void DeleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }
}
