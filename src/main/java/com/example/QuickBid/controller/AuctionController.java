package com.example.QuickBid.controller;

import com.example.QuickBid.dto.AuctionCardDTO;
import com.example.QuickBid.dto.AuctionDTO;
import com.example.QuickBid.dto.AuctionDetailDTO;
import com.example.QuickBid.service.AuctionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @PostMapping(path = "/create", consumes = "multipart/form-data")
    public ResponseEntity<?> createAuction(
            @ModelAttribute AuctionDTO auctionDTO,
            @RequestParam("images") MultipartFile[] images) {

        try {
            if (images.length == 0 || images[0].isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "At least one image is required."));
            }

            auctionService.createAuction(auctionDTO, images);

            Map<String, Object> response = Collections.singletonMap("success", true);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionDetailDTO> getAuctionById(@PathVariable int id) {
        try {
            AuctionDetailDTO auctionDetails = auctionService.getAuctionDetailsById(id);
            return ResponseEntity.ok(auctionDetails);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
