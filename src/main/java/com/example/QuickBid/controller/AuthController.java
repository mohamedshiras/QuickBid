package com.example.QuickBid.controller;

import com.example.QuickBid.model.User;
import com.example.QuickBid.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam("username") String usernameOrEmail,
            @RequestParam("password") String password,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            AuthService.LoginResult result = authService.authenticateUser(usernameOrEmail, password);

            if (result.isSuccess()) {
                // Store user/admin information in session
                if ("USER".equals(result.getUserType())) {
                    session.setAttribute("userId", result.getUser().getId());
                    session.setAttribute("username", result.getUser().getUsername());
                    session.setAttribute("userType", "USER");
                    session.setAttribute("fullname", result.getUser().getFullname());
                } else if ("ADMIN".equals(result.getUserType())) {
                    session.setAttribute("adminId", result.getAdmin().getAdminID());
                    session.setAttribute("username", result.getAdmin().getAdminUsername());
                    session.setAttribute("userType", "ADMIN");
                }

                response.put("success", true);
                response.put("message", result.getMessage());
                response.put("userType", result.getUserType());

                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", result.getMessage());

                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());

            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam("fullname") String fullname,
            @RequestParam("address") String address,
            @RequestParam("number") String contact,  // Note: form uses "number" field name
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirm-password") String confirmPassword) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Validate input fields
            if (fullname == null || fullname.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Full name is required!");
                return ResponseEntity.badRequest().body(response);
            }

            if (address == null || address.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Address is required!");
                return ResponseEntity.badRequest().body(response);
            }

            if (contact == null || contact.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Contact number is required!");
                return ResponseEntity.badRequest().body(response);
            }

            // Validate contact number format (10 digits)
            if (!contact.matches("^[0-9]{10}$")) {
                response.put("success", false);
                response.put("message", "Contact number must be exactly 10 digits!");
                return ResponseEntity.badRequest().body(response);
            }

            if (username == null || username.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Username is required!");
                return ResponseEntity.badRequest().body(response);
            }

            if (email == null || email.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Email is required!");
                return ResponseEntity.badRequest().body(response);
            }

            // Validate email format
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                response.put("success", false);
                response.put("message", "Please enter a valid email address!");
                return ResponseEntity.badRequest().body(response);
            }

            if (password == null || password.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Password is required!");
                return ResponseEntity.badRequest().body(response);
            }

            if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Please confirm your password!");
                return ResponseEntity.badRequest().body(response);
            }

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                response.put("success", false);
                response.put("message", "Passwords do not match!");
                return ResponseEntity.badRequest().body(response);
            }

            // Validate password strength (minimum 4 characters for simplicity)
            if (password.length() < 4) {
                response.put("success", false);
                response.put("message", "Password must be at least 4 characters long!");
                return ResponseEntity.badRequest().body(response);
            }

            // For registration, we'll assume admin with ID 1 approves (you can modify this logic)
            Long whoApproved = 1L;

            User newUser = authService.registerUser(
                    fullname.trim(),
                    address.trim(),
                    contact.trim(),
                    username.trim(),
                    email.trim().toLowerCase(),
                    password,
                    whoApproved
            );

            response.put("success", true);
            response.put("message", "Registration successful! You can now login with your credentials.");
            response.put("userId", newUser.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            session.invalidate();

            response.put("success", true);
            response.put("message", "Logout successful!");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Logout failed: " + e.getMessage());

            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/check-session")
    public ResponseEntity<Map<String, Object>> checkSession(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        String userType = (String) session.getAttribute("userType");
        String username = (String) session.getAttribute("username");

        if (userType != null && username != null) {
            response.put("loggedIn", true);
            response.put("userType", userType);
            response.put("username", username);

            if ("USER".equals(userType)) {
                response.put("userId", session.getAttribute("userId"));
                response.put("fullname", session.getAttribute("fullname"));
            } else if ("ADMIN".equals(userType)) {
                response.put("adminId", session.getAttribute("adminId"));
            }
        } else {
            response.put("loggedIn", false);
        }

        return ResponseEntity.ok(response);
    }
}