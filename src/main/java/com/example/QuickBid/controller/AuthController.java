package com.example.QuickBid.controller;

import com.example.QuickBid.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // User Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam("username") String username,
                                       @RequestParam("password") String password,
                                       HttpSession session) {

        Map<String, Object> response = authService.loginUser(username, password);

        if ((Boolean) response.get("success")) {
            // Store user info in session
            session.setAttribute("userId", response.get("userId"));
            session.setAttribute("username", response.get("username"));
            session.setAttribute("email", response.get("email"));
            session.setAttribute("fullname", response.get("fullname"));
            session.setAttribute("isLoggedIn", true);

            // Return success response
            return ResponseEntity.ok(response);
        } else {
            // Return error response
            return ResponseEntity.badRequest().body(response);
        }
    }

    // User Registration Endpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam("fullname") String fullname,
                                          @RequestParam("address") String address,
                                          @RequestParam("number") String contact,
                                          @RequestParam("username") String username,
                                          @RequestParam("email") String email,
                                          @RequestParam("password") String password,
                                          @RequestParam("confirm-password") String confirmPassword) {

        Map<String, Object> response = authService.registerUser(fullname, address, contact,
                username, email, password, confirmPassword);

        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Admin Login Endpoint
    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdmin(@RequestParam("adminUsername") String adminUsername,
                                        @RequestParam("adminPassword") String adminPassword,
                                        HttpSession session) {

        Map<String, Object> response = authService.loginAdmin(adminUsername, adminPassword);

        if ((Boolean) response.get("success")) {
            // Store admin info in session
            session.setAttribute("adminId", response.get("adminId"));
            session.setAttribute("adminUsername", response.get("adminUsername"));
            session.setAttribute("isAdminLoggedIn", true);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Logout Endpoint
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("success", true, "message", "Logged out successfully"));
    }

    // Check if user is logged in
    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        Boolean isAdminLoggedIn = (Boolean) session.getAttribute("isAdminLoggedIn");

        if (isLoggedIn != null && isLoggedIn) {
            return ResponseEntity.ok(Map.of(
                    "isLoggedIn", true,
                    "userId", session.getAttribute("userId"),
                    "username", session.getAttribute("username"),
                    "email", session.getAttribute("email"),
                    "fullname", session.getAttribute("fullname")
            ));
        } else if (isAdminLoggedIn != null && isAdminLoggedIn) {
            return ResponseEntity.ok(Map.of(
                    "isAdminLoggedIn", true,
                    "adminId", session.getAttribute("adminId"),
                    "adminUsername", session.getAttribute("adminUsername")
            ));
        } else {
            return ResponseEntity.ok(Map.of("isLoggedIn", false));
        }
    }
}