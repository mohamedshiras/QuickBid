package com.example.QuickBid.controller;

import com.example.QuickBid.model.Auction;
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

    // Auction Creation
    @PostMapping
    public ResponseEntity<Map<String, Object>> createAuction(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String category,
            @RequestParam(required = false) String condition,
            @RequestParam BigDecimal startingPrice,
            @RequestParam int duration,
            @RequestParam(required = false) List<MultipartFile> images,
            HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null || !"USER".equals(session.getAttribute("userType"))) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Unauthorized access"
            ));
        }

        try {
            return ResponseEntity.ok(auctionService.createAuction(
                    title, description, category, condition,
                    startingPrice, duration, images, userId
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}