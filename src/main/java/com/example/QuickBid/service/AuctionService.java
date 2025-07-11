package com.example.QuickBid.service;

import com.example.QuickBid.dto.AuctionCardDTO;
import com.example.QuickBid.dto.AuctionDTO;
import com.example.QuickBid.model.Auction;
import com.example.QuickBid.model.AuctionImage;
import com.example.QuickBid.model.User;
import com.example.QuickBid.repository.AuctionImageRepository;
import com.example.QuickBid.repository.AuctionRepository;
import com.example.QuickBid.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private AuctionImageRepository auctionImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Auction createAuction(AuctionDTO auctionDTO, MultipartFile[] images) throws IOException {

        // Fetch the user using the Integer userId
        User user = userRepository.findById(auctionDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + auctionDTO.getUserId()));

        // Create and map DTO to the Auction entity
        Auction auction = new Auction();
        auction.setTitle(auctionDTO.getTitle());
        auction.setDescription(auctionDTO.getDescription());
        auction.setCategory(auctionDTO.getCategory());
        auction.setCondition(auctionDTO.getCondition());
        auction.setStartingPrice(auctionDTO.getStartingPrice());
        auction.setUser(user);

        // Set server-generated values
        auction.setPostedDateAndTime(LocalDateTime.now());
        auction.setAuctionDeadline(LocalDateTime.now().plusDays(auctionDTO.getDuration()));
        auction.setAuctionStatus("pending");

        // Save the auction entity
        Auction savedAuction = auctionRepository.save(auction);

        // Handle and save the images
        if (images != null && images.length > 0) {
            for (MultipartFile imageFile : images) {
                if (!imageFile.isEmpty()) {
                    AuctionImage auctionImage = new AuctionImage();
                    auctionImage.setAuction(savedAuction);
                    auctionImage.setImageData(imageFile.getBytes());
                    auctionImageRepository.save(auctionImage);
                }
            }
        }

        return savedAuction;
    }


    public List<AuctionCardDTO> getActiveAuctions() {
        // 1. Fetch all auctions with the status "active"
        List<Auction> activeAuctions = auctionRepository.findByAuctionStatus("active");

        // 2. Map the entities to DTOs
        return activeAuctions.stream().map(auction -> {
            AuctionCardDTO dto = new AuctionCardDTO();
            dto.setAuctionId(auction.getAuctionId());
            dto.setTitle(auction.getTitle());
            // Truncate description for card view
            if (auction.getDescription().length() > 50) {
                dto.setDescription(auction.getDescription().substring(0, 50) + "...");
            } else {
                dto.setDescription(auction.getDescription());
            }
            dto.setStartingPrice(auction.getStartingPrice());

            // 3. Fetch the first image for the auction
            Optional<AuctionImage> firstImage = auctionImageRepository.findFirstByAuction(auction);
            if (firstImage.isPresent()) {
                // Encode image bytes to a Base64 string to be used directly in HTML
                String base64Image = java.util.Base64.getEncoder().encodeToString(firstImage.get().getImageData());
                // Create a data URL for the image
                dto.setImageUrl("data:image/jpeg;base64," + base64Image);
            } else {
                // Provide a placeholder if no image exists
                dto.setImageUrl("https://placehold.co/300x200/e8e8e8/000?text=No+Image");
            }
            return dto;
        }).collect(java.util.stream.Collectors.toList());
    }
}