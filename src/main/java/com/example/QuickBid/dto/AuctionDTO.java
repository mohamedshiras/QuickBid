package com.example.QuickBid.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for creating a new auction.
 * This class is used to transfer data from the auction creation form (postauction.html)
 * to the controller and service layers. It includes all the fields submitted by the user.
 */
@Getter
@Setter
public class AuctionDTO {

    /**
     * The title of the item being auctioned.
     * Corresponds to the 'title' input field in the form.
     */
    private String title;

    /**
     * A detailed description of the item.
     * Corresponds to the 'description' textarea in the form.
     */
    private String description;

    /**
     * The starting price for the auction.
     * Stored as a String to handle currency formatting and then converted in the service layer.
     * Corresponds to the 'startingPrice' input field.
     */
    private String startingPrice;

    /**
     * The category of the item (e.g., "Electronics", "Art").
     * Corresponds to the 'category' select dropdown.
     */
    private String category;

    /**
     * The condition of the item (e.g., "New", "Used").
     * Corresponds to the 'condition' select dropdown.
     */
    private String condition;

    /**
     * The duration of the auction in days (e.g., 1, 3, 7).
     * This will be used to calculate the auction's end date.
     * Corresponds to the 'duration' select dropdown.
     */
    private int duration;

    /**
     * The ID of the user who is posting the auction.
     * This is a hidden field in the form, populated after the user logs in.
     * Using Integer to match your specified user ID type.
     */
    private Integer userId;

}