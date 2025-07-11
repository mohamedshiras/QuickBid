package com.example.QuickBid.controller;

import com.example.QuickBid.model.Admin;
import com.example.QuickBid.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> requestBody,
                                     HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();

        String username = requestBody.get("username");
        String password = requestBody.get("password");

        System.out.println("DEBUG: Login attempt - Username: " + username + ", Password: " + password);

        // Input validation
        if (username == null || username.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Username is required.");
            return response;
        }

        if (password == null || password.isEmpty()) {
            response.put("success", false);
            response.put("message", "Password is required.");
            return response;
        }

        // Check if admin exists first
        Admin adminCheck = adminService.getByUsername(username);
        System.out.println("DEBUG: Admin found: " + (adminCheck != null ? "Yes" : "No"));
        if (adminCheck != null) {
            System.out.println("DEBUG: Admin details - ID: " + adminCheck.getAdminID() +
                    ", Username: " + adminCheck.getAdminUsername() +
                    ", Password: " + adminCheck.getAdminPassword());
        }

        // Authenticate admin using the service method
        if (adminService.authenticateAdmin(username, password)) {
            // Get the admin object for session storage
            Admin admin = adminService.getByUsername(username.trim());

            // Invalidate any existing session to prevent conflicts
            HttpSession existingSession = request.getSession(false);
            if (existingSession != null) {
                existingSession.invalidate();
            }

            // Create new session for logged in admin
            HttpSession session = request.getSession(true);
            session.setAttribute("loggedInAdmin", admin);

            response.put("success", true);
            response.put("message", "Login successful!");
            response.put("adminId", admin.getAdminID());
            response.put("adminUsername", admin.getAdminUsername());
        } else {
            System.out.println("DEBUG: Authentication failed for username: " + username);
            response.put("success", false);
            response.put("message", "Invalid username or password.");
        }

        return response;
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        if (session != null) {
            session.invalidate();
            response.put("success", true);
            response.put("message", "Logged out successfully.");
        } else {
            response.put("success", false);
            response.put("message", "No active session found.");
        }

        return response;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        if (session == null) {
            response.put("success", false);
            response.put("message", "Unauthorized - No session");
            return response;
        }

        Admin admin = (Admin) session.getAttribute("loggedInAdmin");

        if (admin != null) {
            response.put("success", true);
            response.put("message", "Welcome, " + admin.getAdminUsername());
            response.put("adminId", admin.getAdminID());
            response.put("adminUsername", admin.getAdminUsername());
        } else {
            response.put("success", false);
            response.put("message", "Unauthorized - Please login");
        }

        return response;
    }

    // Additional endpoint to create admin for testing
    @PostMapping("/create")
    public Map<String, Object> createAdmin(@RequestBody Map<String, String> requestBody) {
        Map<String, Object> response = new HashMap<>();

        String username = requestBody.get("username");
        String password = requestBody.get("password");

        if (username == null || username.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Username is required.");
            return response;
        }

        if (password == null || password.isEmpty()) {
            response.put("success", false);
            response.put("message", "Password is required.");
            return response;
        }

        if (adminService.adminExists(username)) {
            response.put("success", false);
            response.put("message", "Admin with this username already exists.");
            return response;
        }

        try {
            Admin admin = adminService.createAdmin(username, password);
            response.put("success", true);
            response.put("message", "Admin created successfully.");
            response.put("adminId", admin.getAdminID());
            response.put("adminUsername", admin.getAdminUsername());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating admin: " + e.getMessage());
        }

        return response;
    }

    // Endpoint to check if admin exists (for testing)
    @GetMapping("/exists/{username}")
    public Map<String, Object> adminExists(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();
        boolean exists = adminService.adminExists(username);
        response.put("exists", exists);
        response.put("username", username);
        return response;
    }

    @GetMapping("/test")
    public String test() {
        return "Controller is working!";
    }
}