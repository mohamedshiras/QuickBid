package com.example.QuickBid.repository;

import com.example.QuickBid.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Integer> {
    List<Auction> findByAuctionStatus(String status);
    List<Auction> findByUser_UserId(int userId);

    List<Auction> findByTitleContainingIgnoreCase(String title);
}