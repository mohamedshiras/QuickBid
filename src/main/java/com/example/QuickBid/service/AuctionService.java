package com.example.QuickBid.service;

import com.example.QuickBid.dto.AuctionAdminDTO;
import com.example.QuickBid.dto.AuctionCardDTO;
import com.example.QuickBid.dto.AuctionDTO;
import com.example.QuickBid.dto.AuctionDetailDTO;
import com.example.QuickBid.dto.SellerInfoDTO;
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
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        User user = userRepository.findById(auctionDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + auctionDTO.getUserId()));

        Auction auction = new Auction();
        auction.setTitle(auctionDTO.getTitle());
        auction.setDescription(auctionDTO.getDescription());
        auction.setCategory(auctionDTO.getCategory());
        auction.setCondition(auctionDTO.getCondition());
        auction.setStartingPrice(auctionDTO.getStartingPrice());
        auction.setUser(user);
        auction.setPostedDateAndTime(LocalDateTime.now());
        auction.setAuctionDeadline(LocalDateTime.now().plusDays(auctionDTO.getDuration()));
        auction.setAuctionStatus("pending");

        Auction savedAuction = auctionRepository.save(auction);

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
        List<Auction> activeAuctions = auctionRepository.findByAuctionStatus("active");
        return activeAuctions.stream().map(auction -> {
            AuctionCardDTO dto = new AuctionCardDTO();
            dto.setAuctionId(auction.getAuctionId());
            dto.setTitle(auction.getTitle());
            if (auction.getDescription().length() > 50) {
                dto.setDescription(auction.getDescription().substring(0, 50) + "...");
            } else {
                dto.setDescription(auction.getDescription());
            }
            dto.setStartingPrice(auction.getStartingPrice());

            Optional<AuctionImage> firstImage = auctionImageRepository.findFirstByAuction(auction);
            if (firstImage.isPresent()) {
                String base64Image = Base64.getEncoder().encodeToString(firstImage.get().getImageData());
                dto.setImageUrl("data:image/jpeg;base64," + base64Image);
            } else {
                dto.setImageUrl("https://placehold.co/300x200/e8e8e8/000?text=No+Image");
            }
            return dto;
        }).collect(Collectors.toList());
    }

    public AuctionDetailDTO getAuctionDetailsById(int auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new EntityNotFoundException("Auction not found with id: " + auctionId));

        List<String> imageUrls = auctionImageRepository.findByAuction(auction).stream()
                .map(image -> "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(image.getImageData()))
                .collect(Collectors.toList());

        User seller = auction.getUser();
        SellerInfoDTO sellerInfoDTO = new SellerInfoDTO();
        sellerInfoDTO.setSellerName(seller.getFullname());

        if (seller.getCreatedAt() != null) {
            sellerInfoDTO.setMemberSince("Member since " + seller.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy")));
        } else {
            sellerInfoDTO.setMemberSince("Member since N/A");
        }

        sellerInfoDTO.setSellerAvatarInitial(seller.getFullname().substring(0, 1).toUpperCase());

        AuctionDetailDTO detailDTO = new AuctionDetailDTO();
        detailDTO.setAuctionId(auction.getAuctionId());
        detailDTO.setTitle(auction.getTitle());
        detailDTO.setDescription(auction.getDescription());
        detailDTO.setCategory(auction.getCategory());
        detailDTO.setCondition(auction.getCondition());
        detailDTO.setStartingPrice(auction.getStartingPrice());
        detailDTO.setCurrentBid(auction.getStartingPrice());
        detailDTO.setAuctionDeadline(auction.getAuctionDeadline());
        detailDTO.setImageUrls(imageUrls);
        detailDTO.setSeller(sellerInfoDTO);

        return detailDTO;
    }

    // --- Methods for Administrative Auction Management ---

    @Transactional(readOnly = true)
    public List<AuctionAdminDTO> getAuctionsByStatus(String status) {
        List<Auction> auctions;
        if (status == null || status.equalsIgnoreCase("all")) {
            auctions = auctionRepository.findAll();
        } else {
            auctions = auctionRepository.findByAuctionStatus(status);
        }

        return auctions.stream().map(auction -> {
            AuctionAdminDTO dto = new AuctionAdminDTO();
            dto.setAuctionId(auction.getAuctionId());
            dto.setTitle(auction.getTitle());
            dto.setSellerName(auction.getUser() != null ? auction.getUser().getUsername() : "N/A");
            dto.setStartingPrice(auction.getStartingPrice());
            dto.setAuctionStatus(auction.getAuctionStatus());

            Optional<AuctionImage> firstImage = auctionImageRepository.findFirstByAuction(auction);
            if (firstImage.isPresent()) {
                String base64Image = Base64.getEncoder().encodeToString(firstImage.get().getImageData());
                dto.setImageUrl("data:image/jpeg;base64," + base64Image);
            } else {
                dto.setImageUrl("https://placehold.co/60x60/e8e8e8/000?text=N/A");
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AuctionAdminDTO> searchAuctionsByTitle(String title) {
        List<Auction> auctions = auctionRepository.findByTitleContainingIgnoreCase(title);
        // Re-use the mapping logic from getAuctionsByStatus
        return auctions.stream().map(auction -> {
            AuctionAdminDTO dto = new AuctionAdminDTO();
            dto.setAuctionId(auction.getAuctionId());
            dto.setTitle(auction.getTitle());
            dto.setSellerName(auction.getUser() != null ? auction.getUser().getUsername() : "N/A");
            dto.setStartingPrice(auction.getStartingPrice());
            dto.setAuctionStatus(auction.getAuctionStatus());

            Optional<AuctionImage> firstImage = auctionImageRepository.findFirstByAuction(auction);
            if (firstImage.isPresent()) {
                String base64Image = Base64.getEncoder().encodeToString(firstImage.get().getImageData());
                dto.setImageUrl("data:image/jpeg;base64," + base64Image);
            } else {
                dto.setImageUrl("https://placehold.co/60x60/e8e8e8/000?text=N/A");
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void updateAuctionStatus(Integer auctionId, String status) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new EntityNotFoundException("Auction not found with id: " + auctionId));

        auction.setAuctionStatus(status);
        auctionRepository.save(auction);
    }

    @Transactional
    public void deleteAuction(Integer auctionId) {
        // Find the auction to be deleted, or throw an exception if it doesn't exist
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new EntityNotFoundException("Auction not found with id: " + auctionId));

        // Find all images associated with this auction
        List<AuctionImage> images = auctionImageRepository.findByAuction(auction);

        // Delete all the associated images first to prevent constraint violation errors
        if (images != null && !images.isEmpty()) {
            auctionImageRepository.deleteAll(images);
        }

        // Finally, delete the auction itself
        auctionRepository.delete(auction);
    }
}
