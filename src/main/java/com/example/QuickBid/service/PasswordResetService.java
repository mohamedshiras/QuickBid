package com.example.QuickBid.service;

import com.example.QuickBid.model.Token;
import com.example.QuickBid.model.User;
import com.example.QuickBid.repository.PasswordResetTokenRepository;
import com.example.QuickBid.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public void initiatePasswordReset(String email) {
        // 1. Check if user exists
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        // 2. Generate 6-digit OTP token
        String otp = String.format("%06d", new Random().nextInt(999999));

        // 3. Create Token entity and save
        Token token = new Token();
        token.setEmail(email);
        token.setToken(otp);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(15));  // Token valid for 15 mins
        tokenRepository.save(token);

        // 4. Send OTP email using EmailService
        try {
            emailService.sendOtpEmail(email, otp);
        } catch (Exception e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
            // You might want to delete the token if email fails
            // tokenRepository.delete(token);
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    public boolean validateResetTokenByEmailAndOtp(String email, String otp) {
        Optional<Token> optionalToken = tokenRepository.findByEmailAndToken(email, otp);

        if (optionalToken.isEmpty()) {
            return false; // no such token
        }
        Token token = optionalToken.get();

        // Check if token expired
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        // Optionally mark the token as used here if needed

        return true;
    }

    public void resetPasswordByEmail(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(newPassword);  // Store password as plain text
        userRepository.save(user);
    }
}