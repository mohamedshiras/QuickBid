package com.example.QuickBid.service;

import com.example.QuickBid.dto.PaymentRequest;
import com.example.QuickBid.dto.PaymentResponse;
import com.example.QuickBid.model.*;
import com.example.QuickBid.repository.AuctionRepository;
import com.example.QuickBid.repository.BidRepository;
import com.example.QuickBid.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private BidRepository bidRepository;

    @Transactional
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        // 1. Get the auction
        Auction auction = auctionRepository.findById(paymentRequest.getAuctionId())
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        // 2. Update auction status and ended date
        auction.setAuctionStatus("ended");
        auction.setEndedDateAndTime(LocalDateTime.now());
        auctionRepository.save(auction);

        // 3. Get the winning bid (highest bid for this auction)
        Bid winningBid = bidRepository.findTopByAuctionOrderByBidAmountDesc(auction)
                .orElseThrow(() -> new RuntimeException("No bids found for this auction"));

        // 4. Create and save transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(paymentRequest.getAmount());
        transaction.setPaymentDateandTime(LocalDateTime.now());
        transaction.setBid(winningBid);

        Transaction savedTransaction = transactionRepository.save(transaction);

        // 5. Prepare response
        return new PaymentResponse(
                savedTransaction.getTransactionId(),
                "Payment processed successfully",
                true
        );
    }
}