package com.example.QuickBid.service;

import com.example.QuickBid.model.*;
import com.example.QuickBid.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final AuctionImageRepository auctionImageRepository;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository,
                          UserRepository userRepository,
                          AuctionImageRepository auctionImageRepository) {
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
        this.auctionImageRepository = auctionImageRepository;
    }

    @Transactional
    public Map<String, Object> createAuction(
            String title, String description, String category, String condition,
            BigDecimal startingPrice, int duration, List<MultipartFile> images,
            int userId) {

        // 1. Validate required fields
        validateAuctionData(title, description, startingPrice, duration);

        // 2. Fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // 3. Create and save auction
        Auction auction = buildAuctionEntity(title, description, category, condition,
                startingPrice, duration, user);
        Auction savedAuction = auctionRepository.save(auction);

        // 4. Process and save images
        if (images != null && !images.isEmpty()) {
            validateImages(images);
            saveAuctionImages(images, savedAuction);
        }

        return Map.of(
                "success", true,
                "message", "Auction created successfully",
                "auctionId", savedAuction.getAuctionId(),
                "imageCount", images != null ? images.size() : 0
        );
    }

    private void validateAuctionData(String title, String description,
                                     BigDecimal startingPrice, int duration) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (startingPrice == null || startingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Starting price must be positive");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
    }

    private Auction buildAuctionEntity(String title, String description, String category,
                                       String condition, BigDecimal startingPrice,
                                       int duration, User user) {
        Auction auction = new Auction();
        auction.setTitle(title);
        auction.setDescription(description);
        auction.setCategory(category);
        auction.setCondition(condition);
        auction.setStartingPrice(startingPrice.toString());
        auction.setAuctionStatus("pending confirmation");

        LocalDateTime now = LocalDateTime.now();
        auction.setPostedDateAndTime(now);
        auction.setAuctionDeadline(now.plusDays(duration));

        auction.setUser(user);
        auction.setAdmin(null); // Admin approval happens later

        return auction;
    }

    private void validateImages(List<MultipartFile> images) {
        // Validate individual images
        for (MultipartFile image : images) {
            if (image.isEmpty()) {
                throw new IllegalArgumentException("One or more image files are empty");
            }
            if (image.getSize() > 5 * 1024 * 1024) { // 5MB limit
                throw new IllegalArgumentException(
                        "Image " + image.getOriginalFilename() + " exceeds 5MB size limit");
            }
            if (!List.of("image/jpeg", "image/png", "image/gif")
                    .contains(image.getContentType())) {
                throw new IllegalArgumentException(
                        "Unsupported image type: " + image.getContentType());
            }
        }

        // Validate total count
        if (images.size() > 5) {
            throw new IllegalArgumentException("Maximum 5 images allowed");
        }
    }

    private void saveAuctionImages(List<MultipartFile> images, Auction auction) {
        List<AuctionImage> auctionImages = images.stream()
                .map(image -> {
                    try {
                        AuctionImage auctionImage = new AuctionImage();
                        auctionImage.setAuction(auction);
                        auctionImage.setImageData(image.getBytes());
                        return auctionImage;
                    } catch (IOException e) {
                        throw new RuntimeException(
                                "Failed to process image: " + image.getOriginalFilename(), e);
                    }
                })
                .collect(Collectors.toList());

        auctionImageRepository.saveAll(auctionImages);
    }

    // Additional useful methods
    public List<AuctionImage> getAuctionImages(int auctionId) {
        return auctionImageRepository.findByAuction_AuctionId(auctionId);
    }

    @Transactional
    public void deleteAuction(int auctionId) {
        // First delete images to maintain referential integrity
        auctionImageRepository.deleteByAuction_AuctionId(auctionId);
        auctionRepository.deleteById(auctionId);
    }
}