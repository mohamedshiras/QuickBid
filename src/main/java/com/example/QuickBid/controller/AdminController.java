package com.example.QuickBid.controller;

import com.example.QuickBid.dto.AdminLoginRequest;
import com.example.QuickBid.dto.AdminLoginResponse;
import com.example.QuickBid.dto.AdminSignupRequest;
import com.example.QuickBid.model.Admin;
import com.example.QuickBid.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(@RequestBody AdminLoginRequest loginRequest) {
        Admin admin = adminService.authenticateAdmin(
                loginRequest.getAdminUsername(),
                loginRequest.getAdminPassword());

        AdminLoginResponse response = new AdminLoginResponse();
        response.setAdminID(admin.getAdminID());
        response.setAdminUsername(admin.getAdminUsername());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<AdminLoginResponse> signup(@RequestBody AdminSignupRequest signupRequest) {
        Admin admin = adminService.registerAdmin(
                signupRequest.getAdminUsername(),
                signupRequest.getAdminPassword());

        AdminLoginResponse response = new AdminLoginResponse();
        response.setAdminID(admin.getAdminID());
        response.setAdminUsername(admin.getAdminUsername());

        return ResponseEntity.ok(response);
    }
}