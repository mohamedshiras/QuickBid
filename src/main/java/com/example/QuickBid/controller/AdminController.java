package com.example.QuickBid.controller;

import com.example.QuickBid.dto.AuctionAdminDTO;
import com.example.QuickBid.model.Admin;
import com.example.QuickBid.model.User;
import com.example.QuickBid.service.AdminService;
import com.example.QuickBid.service.AuctionService;
import com.example.QuickBid.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuctionService auctionService;


    // --- User Management Endpoints ---

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String status) {
        // CORRECTED: This now properly handles requests for "all" users vs. a specific status.
        List<User> users;
        if (status == null || "all".equalsIgnoreCase(status)) {
            users = userService.getAllUsers();
        } else {
            users = userService.getUsersByStatus(status);
        }
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        try {
            userService.updateUserStatus(id, body.get("status"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            userService.DeleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- Auction Management Endpoints ---

    @GetMapping("/auctions")
    public ResponseEntity<List<AuctionAdminDTO>> getAuctions(@RequestParam(required = false) String status) {
        List<AuctionAdminDTO> auctions = auctionService.getAuctionsByStatus(status);
        return ResponseEntity.ok(auctions);
    }

    @PutMapping("/auctions/{id}/status")
    public ResponseEntity<?> updateAuctionStatus(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        try {
            auctionService.updateAuctionStatus(id, body.get("status"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/auctions/{id}")
    public ResponseEntity<?> deleteAuction(@PathVariable Integer id) {
        try {
            auctionService.deleteAuction(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
