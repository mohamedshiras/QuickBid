package com.example.QuickBid.service;

import com.example.QuickBid.model.Admin;
import com.example.QuickBid.model.Auction;
import com.example.QuickBid.model.User;
import com.example.QuickBid.repository.AdminRepository;
import com.example.QuickBid.repository.AuctionRepository;
import com.example.QuickBid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    // In a real app, you would have a service to handle file storage (e.g., to S3)
    // For now, we'll just log the image names.

    public Map<String, Object> createAuction(
            String title, String description, String category, String condition,
            BigDecimal startingPrice, int duration, List<MultipartFile> images,
            long userId, long adminId) {

        // 1. Fetch the related User and Admin entities
        User postingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Admin approvingAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));

        // 2. Create a new Auction instance
        Auction newAuction = new Auction();
        newAuction.setTitle(title);
        newAuction.setDescription(description);
        newAuction.setCategory(category);
        newAuction.setCondition(condition);
        newAuction.setStartingPrice(startingPrice.toString()); // Assuming your entity still uses String for price
        newAuction.setEndingPrice(null); // Ending price is determined when the auction ends

        // 3. Calculate the deadline
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = now.plusDays(duration);
        newAuction.setAuctionDeadline(deadline);
        newAuction.setEndedDateAndTime(deadline); // Set endedDateandTime to the same as deadline initially

        // 4. Set the relationships
        newAuction.setUser(postingUser);
        newAuction.setAdmin(approvingAdmin);

        // The @PrePersist annotation will handle setting the status and postedDateandTime

        // 5. Save the auction to the database
        Auction savedAuction = auctionRepository.save(newAuction);

        // 6. Handle image uploads (in a real app, save them to cloud storage and link to the auction)
        if (images != null && !images.isEmpty()) {
            System.out.println("Received " + images.size() + " images for auction ID: " + savedAuction.getAuctionId());
            for (MultipartFile image : images) {
                System.out.println(" - " + image.getOriginalFilename());
                // Here you would call a file storage service: fileStorageService.save(image, savedAuction.getId());
            }
        }

        // 7. Return a success response
        return Map.of("success", true, "message", "Auction posted successfully!", "auctionId", savedAuction.getAuctionId());
    }
}
