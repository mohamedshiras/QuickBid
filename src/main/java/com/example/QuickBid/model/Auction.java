package com.example.QuickBid.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "auctions")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long auctionId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(nullable = false, length = 10)
    private String startingPrice;

    @Column(nullable = false, length = 10)
    private String endingPrice;

    @Column(name = "postedDateandTime")
    private LocalDateTime postedDateandTime;

    @Column(nullable = false, name = "endedDateandTime")
    private LocalDateTime endedDateandTime;

    @Column(columnDefinition = "DEFAULT 'pending confirmation'")
    private String auctionStatus;

    @Column(nullable = false, length = 25)
    private String catagory;

    @Column(nullable = false)
    private LocalDateTime auctionDeadline;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "whoPosted")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "whoApproved")
    private Admin admin;

}
