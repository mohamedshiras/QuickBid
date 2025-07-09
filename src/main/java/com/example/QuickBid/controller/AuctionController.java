package com.example.QuickBid.controller;

import com.example.QuickBid.service.AuctionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createAuction(
            @RequestParam("itemTitle") String title,
            @RequestParam("itemDescription") String description,
            @RequestParam("itemCategory") String category,
            @RequestParam(value = "itemCondition", required = false) String condition,
            @RequestParam("startingPrice") BigDecimal startingPrice,
            @RequestParam("auctionDuration") int duration,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            HttpSession session) {

        // Check if user is logged in
        String userType = (String) session.getAttribute("userType");
        if (!"USER".equals(userType)) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "You must be logged in as a user to create an auction"
            ));
        }

        // Get user ID from session
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "User session invalid"
            ));
        }

        try {
            Map<String, Object> response = auctionService.createAuction(
                    title, description, category, condition,
                    startingPrice, duration, images, userId
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}