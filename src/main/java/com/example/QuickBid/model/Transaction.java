package com.example.QuickBid.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;

    @Column(nullable = false, length = 10)
    private String amount;

    @Column(nullable = false, name = "paymentDateandTime")
    private LocalDateTime paymentDateandTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "bidId")
    private Bid bid;
}
