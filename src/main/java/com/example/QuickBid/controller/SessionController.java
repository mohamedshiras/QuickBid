package com.example.QuickBid.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/session")
public class SessionController {

    // Check if user is authenticated
    @GetMapping("/check")
    @ResponseBody
    public ResponseEntity<?> checkSession(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        Boolean isAdminLoggedIn = (Boolean) session.getAttribute("isAdminLoggedIn");

        if (isLoggedIn != null && isLoggedIn) {
            response.put("authenticated", true);
            response.put("userType", "user");
            response.put("userId", session.getAttribute("userId"));
            response.put("username", session.getAttribute("username"));
            response.put("email", session.getAttribute("email"));
            response.put("fullname", session.getAttribute("fullname"));
        } else if (isAdminLoggedIn != null && isAdminLoggedIn) {
            response.put("authenticated", true);
            response.put("userType", "admin");
            response.put("adminId", session.getAttribute("adminId"));
            response.put("adminUsername", session.getAttribute("adminUsername"));
        } else {
            response.put("authenticated", false);
            response.put("userType", null);
        }

        return ResponseEntity.ok(response);
    }

    // Get user profile information
    @GetMapping("/profile")
    @ResponseBody
    public ResponseEntity<?> getUserProfile(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");

        if (isLoggedIn != null && isLoggedIn) {
            response.put("success", true);
            response.put("userId", session.getAttribute("userId"));
            response.put("username", session.getAttribute("username"));
            response.put("email", session.getAttribute("email"));
            response.put("fullname", session.getAttribute("fullname"));
        } else {
            response.put("success", false);
            response.put("message", "User not logged in");
        }

        return ResponseEntity.ok(response);
    }

    // Invalidate session (logout)
    @PostMapping("/invalidate")
    @ResponseBody
    public ResponseEntity<?> invalidateSession(HttpSession session) {
        session.invalidate();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Session invalidated successfully");

        return ResponseEntity.ok(response);
    }

    // Update session attributes
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<?> updateSession(@RequestParam Map<String, String> updates,
                                           HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");

        if (isLoggedIn != null && isLoggedIn) {
            // Update allowed attributes
            for (Map.Entry<String, String> entry : updates.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                // Only allow updating certain attributes
                if (key.equals("fullname") || key.equals("email")) {
                    session.setAttribute(key, value);
                }
            }

            response.put("success", true);
            response.put("message", "Session updated successfully");
        } else {
            response.put("success", false);
            response.put("message", "User not logged in");
        }

        return ResponseEntity.ok(response);
    }
}