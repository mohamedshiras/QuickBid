package com.example.QuickBid.controller;

import com.example.QuickBid.service.PasswordResetService;
import com.example.QuickBid.service.UserService; // Import UserService
import jakarta.servlet.http.HttpSession; // Import HttpSession
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // All password-related endpoints are under this path
public class ResetPasswordController {

    @Autowired
    private PasswordResetService passwordResetService;

    // NEW: Inject UserService to handle password changes for logged-in users
    @Autowired
    private UserService userService;

    // --- Forgot Password Flow (for logged-out users) ---

    @PostMapping("/send-reset-otp")
    public ResponseEntity<?> sendResetOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            passwordResetService.initiatePasswordReset(email);
            return ResponseEntity.ok(Map.of("success", true, "message", "OTP sent to your email"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/verify-reset-otp")
    public ResponseEntity<?> verifyResetOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        boolean isValid = passwordResetService.validateResetTokenByEmailAndOtp(email, otp);
        if (!isValid) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid or expired OTP"));
        }
        return ResponseEntity.ok(Map.of("success", true, "message", "OTP is valid"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        try {
            passwordResetService.resetPasswordByEmail(email, newPassword);
            return ResponseEntity.ok(Map.of("success", true, "message", "Password reset successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/resend-reset-otp")
    public ResponseEntity<?> resendResetOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            passwordResetService.initiatePasswordReset(email);
            return ResponseEntity.ok(Map.of("success", true, "message", "New OTP sent to your email"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // --- Change Password for Logged-In User (from manage-account.html) ---

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "User not authenticated. Please log in.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        try {
            userService.changePassword(userId, currentPassword, newPassword);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Password changed successfully.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Catches specific errors like "Incorrect current password."
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Catches any other unexpected errors
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "An unexpected error occurred.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
