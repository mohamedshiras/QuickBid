package com.example.QuickBid.controller;

import com.example.QuickBid.service.AuctionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class BidController {

    @Autowired
    private AuctionService auctionService;

    @PostMapping("/auctions/{auctionId}/bids")
    public ResponseEntity<?> placeBid(
            @PathVariable int auctionId,
            @RequestBody Map<String, String> payload,
            HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "You must be logged in to place a bid."));
        }

        try {
            String amount = payload.get("amount");
            if (amount == null || amount.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Bid amount cannot be empty."));
            }

            auctionService.placeBid(auctionId, userId, amount);

            return ResponseEntity.ok(Map.of("success", true, "message", "Bid placed successfully!"));

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid bid amount."));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", "An unexpected error occurred."));
        }
    }
}
