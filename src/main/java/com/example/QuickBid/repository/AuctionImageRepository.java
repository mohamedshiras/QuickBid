package com.example.QuickBid.repository;

import com.example.QuickBid.model.Auction;
import com.example.QuickBid.model.AuctionImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionImageRepository extends CrudRepository<AuctionImage, Integer> {

    Optional<AuctionImage> findFirstByAuction(Auction auction);

    // CORRECTED: The return type is now List<AuctionImage>
    List<AuctionImage> findByAuction(Auction auction);

    // These methods seem unused in the current context but are kept for completeness
    List<AuctionImage> findByAuction_AuctionId(int auctionId);

    void deleteByAuction_AuctionId(int auctionId);
}
