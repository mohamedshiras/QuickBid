package com.example.QuickBid.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionAdminDTO {
    private int auctionId;
    private String title;
    private String sellerName;
    private String startingPrice;
    private String auctionStatus;
    private String imageUrl; // To hold the Base64 image data URL
}
