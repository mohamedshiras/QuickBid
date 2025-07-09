package com.example.QuickBid.repository;

import com.example.QuickBid.model.AuctionImage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuctionImageRepository extends CrudRepository<AuctionImage, Integer> {
    List<AuctionImage> findByAuction_AuctionId(int auctionId);

    void deleteByAuction_AuctionId(int auctionId);
}
