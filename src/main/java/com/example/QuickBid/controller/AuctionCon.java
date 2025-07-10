package com.example.QuickBid.controller;

import com.example.QuickBid.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class AuctionCon {

    @Autowired
    private AuctionService auctionService;

    @PostMapping("/postauction")
    public ResponseEntity<Map<String, Object>> postAuction(
            @RequestParam("itemTitle") String title,
            @RequestParam("itemDescription") String description,
            @RequestParam("itemCategory") String category,
            @RequestParam(value = "itemCondition", required = false) String condition, // Condition is optional
            @RequestParam("startingPrice") BigDecimal startingPrice, // Use BigDecimal for money
            @RequestParam("auctionDuration") int duration,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) { // Use MultipartFile for images

        // --- Assume you get these from the logged-in user's security context ---
        long userId = 1L; // Placeholder for the logged-in user's ID
        long adminId = 1L; // Placeholder for a default or assigned admin ID

        try {
            // Delegate all logic to the service layer
            Map<String, Object> response = auctionService.createAuction(
                    title, description, category, condition, startingPrice, duration, images, userId, adminId
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Basic error handling
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}