package com.example.QuickBid.controller;

import com.example.QuickBid.dto.AuctionCardDTO;
import com.example.QuickBid.dto.AuctionDTO;
import com.example.QuickBid.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/auctions") // Matching the form action
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @PostMapping(path = "/create", consumes = "multipart/form-data")
    public ResponseEntity<?> createAuction(
            @ModelAttribute AuctionDTO auctionDTO,
            @RequestParam("images") MultipartFile[] images) {

        try {
            // Check if at least one image is uploaded
            if (images.length == 0 || images[0].isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "At least one image is required."));
            }

            auctionService.createAuction(auctionDTO, images);

            // The success response should match what your JS expects
            Map<String, Object> response = Collections.singletonMap("success", true);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            // Provide a meaningful error response
            e.printStackTrace(); // Log the full error for debugging
            Map<String, Object> errorResponse = Map.of(
                    "success", false,
                    "message", "Error creating auction: " + e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<AuctionCardDTO>> getActiveAuctions() {
        try {
            List<AuctionCardDTO> auctions = auctionService.getActiveAuctions();
            if (auctions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(auctions);
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}