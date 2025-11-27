package com.example.QuickBid.service;

import com.example.QuickBid.model.Auction;
import com.example.QuickBid.model.Bid;
import com.example.QuickBid.model.User;
import com.example.QuickBid.repository.AuctionRepository;
import com.example.QuickBid.repository.BidRepository;
import com.example.QuickBid.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Bid placeBid(int auctionId, int userId, String amountStr) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new EntityNotFoundException("Auction not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (LocalDateTime.now().isAfter(auction.getAuctionDeadline())) {
            throw new IllegalStateException("This auction has already ended.");
        }

        if (user.getUserId().equals(auction.getUser().getUserId())) {
            throw new IllegalStateException("You cannot bid on your own auction.");
        }

        BigDecimal newBidAmount = new BigDecimal(amountStr);

        List<Bid> bids = bidRepository.findByAuction_AuctionIdOrderByBidAmountDesc(auctionId);
        BigDecimal currentHighestBid = bids.isEmpty() ? new BigDecimal(auction.getStartingPrice()) : new BigDecimal(bids.get(0).getBidAmount());

        if (newBidAmount.compareTo(currentHighestBid) <= 0) {
            throw new IllegalArgumentException("Your bid must be higher than the current bid of Rs." + currentHighestBid.toPlainString());
        }

        Bid newBid = new Bid();
        newBid.setAuction(auction);
        newBid.setUser(user);
        newBid.setBidAmount(amountStr);
        newBid.setBidDate(LocalDateTime.now());

        return bidRepository.save(newBid);
    }
}
