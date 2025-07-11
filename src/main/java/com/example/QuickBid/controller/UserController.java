package com.example.QuickBid.controller;

import com.example.QuickBid.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user") // All endpoints here are for the logged-in user
public class UserController {

    @Autowired
    private UserService userService;

    private ResponseEntity<Map<String, Object>> createErrorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<Map<String, Object>> uploadProfilePicture(
            @RequestParam("profilePicture") MultipartFile file,
            HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return createErrorResponse("User not authenticated.", HttpStatus.UNAUTHORIZED);
        }

        try {
            userService.updateProfilePicture(userId, file);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profile picture updated successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse("Failed to upload picture: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/profile-picture")
    public ResponseEntity<Map<String, Object>> removeProfilePicture(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return createErrorResponse("User not authenticated.", HttpStatus.UNAUTHORIZED);
        }

        try {
            userService.removeProfilePicture(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profile picture removed successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse("Failed to remove picture: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return createErrorResponse("User not authenticated.", HttpStatus.UNAUTHORIZED);
        }

        try {
            userService.changePassword(userId, currentPassword, newPassword);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Password changed successfully.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return createErrorResponse("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
