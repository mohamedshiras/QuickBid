package com.example.QuickBid.repository;

import com.example.QuickBid.model.Auction;
import com.example.QuickBid.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {

    List<Bid> findByAuction_AuctionIdOrderByBidAmountDesc(int auctionId);

    Optional<Bid> findTopByAuctionOrderByBidAmountDesc(Auction auction);
}
