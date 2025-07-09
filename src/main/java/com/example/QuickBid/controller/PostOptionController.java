package com.example.QuickBid.controller;

import com.example.QuickBid.model.PostOption;
import com.example.QuickBid.model.User;
import com.example.QuickBid.service.PostOptionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.QuickBid.repository.UserRepository; // Add this import


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/auctions")

public class PostOptionController {

    @Autowired
    private PostOptionService postOptionService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAuctions() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<PostOption> auctions = postOptionService.getAllAuctions();
            response.put("success", true);
            response.put("data", auctions);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to retrieve auctions: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAuction(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            PostOption auction = postOptionService.getAuctionById(id);
            response.put("success", true);
            response.put("data", auction);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to retrieve auction with id " + id + ": " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createAuction(@RequestBody PostOption postOption) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 1. Validate required fields
            if (postOption.getTitle() == null || postOption.getTitle().isBlank() ||
                    postOption.getDescription() == null || postOption.getDescription().isBlank()) {
                throw new IllegalArgumentException("Title and description are required");
            }

            // 2. Get user (don't create inside transaction)
            User user = userRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));


            // 3. Set default values
            if (postOption.getStartingPrice() == null) {
                postOption.setStartingPrice(BigDecimal.ZERO);
            }
            if (postOption.getCurrentBid() == null) {
                postOption.setCurrentBid(postOption.getStartingPrice());
            }

            // 4. Set default times if not provided
            LocalDateTime now = LocalDateTime.now();
            if (postOption.getStartTime() == null) {
                postOption.setStartTime(now);
            }
            if (postOption.getEndTime() == null) {
                postOption.setEndTime(now.plusDays(7));
            }

            // 5. Validate times
            if (postOption.getStartTime().isAfter(postOption.getEndTime())) {
                throw new IllegalArgumentException("Start time must be before end time");
            }

            // 6. Set relationships
            postOption.setCreatedBy(user);

            // 7. Save auction (transactional happens in service layer)
            PostOption savedAuction = postOptionService.saveAuction(postOption);

            response.put("success", true);
            response.put("message", "Auction created successfully");
            response.put("data", savedAuction);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to create auction: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }


    }

    private String encodePassword(String rawPassword) {
        // Implement proper password encoding
        return "{noop}" + rawPassword; // Temporary for development only
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAuction(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            postOptionService.deleteAuction(id);
            response.put("success", true);
            response.put("message", "Auction deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to delete auction with id " + id + ": " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

}