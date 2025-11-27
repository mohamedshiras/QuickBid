package com.example.QuickBid.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionCardDTO {
    private int auctionId;
    private String title;
    private String description;
    private String startingPrice;
    private String imageUrl; // Will hold the Base64 encoded image string
}