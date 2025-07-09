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
    private int auctionId;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 150)
    private String description;

    @Column(nullable = false)
    private String startingPrice;

    @Column(nullable = true)
    private String endingPrice;

    @Column(nullable = false, name = "postedDateAndTime")
    private LocalDateTime postedDateAndTime;

    @Column(nullable = true, name = "endedDateAndTime")
    private LocalDateTime endedDateAndTime;

    @Column(nullable = false)
    private String auctionStatus = "pending confirmation";

    @Column(nullable = false, length = 25)
    private String category;

    @Column(nullable = false, length = 15, name = "item_condition")
    private String condition;

    @Column(nullable = false)
    private LocalDateTime auctionDeadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "whoPosted")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true, name = "whoApproved")
    private Admin admin;
}