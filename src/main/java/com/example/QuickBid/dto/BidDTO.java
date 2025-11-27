package com.example.QuickBid.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class BidDTO {
    private String bidderName;
    private String bidAmount;
    private LocalDateTime bidDate;
    private String bidderAvatarInitial;
}
