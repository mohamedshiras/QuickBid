package com.example.QuickBid.service;

import com.example.QuickBid.model.Token;
import com.example.QuickBid.model.User;
import com.example.QuickBid.repository.PasswordResetTokenRepository;
import com.example.QuickBid.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;


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

        // 4. Send OTP email
        // Note: You can use your EmailService here to send the OTP as plain text or a reset link
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Your QuickBid OTP Code");
        message.setText("Your OTP code is: " + otp + "\nThis OTP will expire in 15 minutes.");

        mailSender.send(message);
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

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
