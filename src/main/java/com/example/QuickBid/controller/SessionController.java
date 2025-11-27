package com.example.QuickBid.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

// SessionController.java
@RestController
@RequestMapping("/api/session")
public class SessionController {

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getSessionUser(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Integer userId = (Integer) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");

        if (userId != null) {
            response.put("loggedIn", true);
            response.put("userId", userId);
            response.put("username", username);
        } else {
            response.put("loggedIn", false);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    public ResponseEntity<Map<String, Object>> getSessionAdmin(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Integer adminId = (Integer) session.getAttribute("adminId");
        String adminUsername = (String) session.getAttribute("adminUsername");
        String userType = (String) session.getAttribute("userType");

        if (adminId != null && "ADMIN".equals(userType)) {
            response.put("loggedIn", true);
            response.put("adminId", adminId);
            response.put("adminUsername", adminUsername);
        } else {
            response.put("loggedIn", false);
        }

        return ResponseEntity.ok(response);
    }
}
