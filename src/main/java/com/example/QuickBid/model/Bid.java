package com.example.QuickBid.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "bids")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bidId;

    @Column(nullable = false, name = "bidDate")
    private LocalDateTime bidDate;

    @Column(nullable = false, length = 10)
    private String bidAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "auctionId")
    private Auction auction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "userId")
    private User user;
}
