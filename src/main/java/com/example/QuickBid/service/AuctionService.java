package com.example.QuickBid.service;

import com.example.QuickBid.model.*;
import com.example.QuickBid.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.*;

@Service
public class AuctionService {
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;

    // Auction Creation
    @Transactional
    public Map<String, Object> createAuction(
            String title, String description, String category, String condition,
            BigDecimal startingPrice, int duration, List<MultipartFile> images,
            int userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Auction auction = new Auction();
        auction.setTitle(title);
        auction.setDescription(description);
        auction.setCategory(category);
        auction.setCondition(condition);
        auction.setStartingPrice(startingPrice.toString());
        auction.setAuctionStatus("pending");
        auction.setUser(user);

        Auction savedAuction = auctionRepository.save(auction);

        return Map.of(
                "success", true,
                "message", "Auction created",
                "auctionId", savedAuction.getAuctionId()
        );
    }
}