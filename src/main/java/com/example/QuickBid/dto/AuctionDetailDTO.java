package com.example.QuickBid.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AuctionDetailDTO {
    private int auctionId;
    private String title;
    private String description;
    private String category;
    private String condition;
    private String startingPrice;
    private String currentBid;
    private LocalDateTime auctionDeadline;
    private List<String> imageUrls;
    private SellerInfoDTO seller;

    // NEW: Added the list to hold bid history
    private List<BidDTO> bids;
}
