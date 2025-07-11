package com.example.QuickBid.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private int auctionId;
    private String cardNumber;
    private String cardName;
    private String cardExpiry;
    private String cardCvv;
    private String billingAddress;
    private String billingCity;
    private String billingZip;
    private String billingCountry;
    private String amount;
}