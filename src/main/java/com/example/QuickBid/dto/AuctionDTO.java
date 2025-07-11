package com.example.QuickBid.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionDTO {

    private String title;
    private String description;
    private String startingPrice;
    private String category;
    private String condition;
    private int duration;
    private Integer userId;

}