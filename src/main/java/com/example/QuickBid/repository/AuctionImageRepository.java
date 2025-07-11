package com.example.QuickBid.repository;

import com.example.QuickBid.model.Auction;
import com.example.QuickBid.model.AuctionImage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuctionImageRepository extends CrudRepository<AuctionImage, Integer> {
    List<AuctionImage> findByAuction_AuctionId(int auctionId);

    void deleteByAuction_AuctionId(int auctionId);

    Optional<AuctionImage> findFirstByAuction(Auction auction);
}
