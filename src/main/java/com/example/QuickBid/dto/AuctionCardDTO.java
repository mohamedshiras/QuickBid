package com.example.QuickBid.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for displaying auction information on the homepage cards.
 * It contains only the essential fields needed for the card view.
 */
@Getter
@Setter
public class AuctionCardDTO {
    private int auctionId;
    private String title;
    private String description;
    private String startingPrice;
    private String imageUrl; // Will hold the Base64 encoded image string
}