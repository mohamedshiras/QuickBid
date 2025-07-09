package com.example.QuickBid.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "auctionImages")
public class AuctionImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imgId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Auction auction;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] imageData;
}
