package com.example.QuickBid.controller;

import com.example.QuickBid.model.Admin;
import com.example.QuickBid.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin") // Note the different base path
public class AdminLoginController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();
        Admin authenticatedAdmin = adminService.authenticateAdmin(username, password);

        if (authenticatedAdmin != null) {
            session.setAttribute("adminId", authenticatedAdmin.getAdminID());
            session.setAttribute("adminUsername", authenticatedAdmin.getAdminUsername());
            session.setAttribute("userType", "ADMIN");

            response.put("success", true);
            response.put("message", "Admin login successful");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Invalid admin username or password");
            return ResponseEntity.status(401).body(response);
        }
    }
}
