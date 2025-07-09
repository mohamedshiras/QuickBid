package com.example.QuickBid.service;

import com.example.QuickBid.model.PostOption;
import com.example.QuickBid.repository.PostOptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostOptionService {

    @Autowired
    private PostOptionRepository repository;

    public List<PostOption> getAllAuctions() {
        return repository.findAll();
    }

    public PostOption getAuctionById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public PostOption saveAuction(PostOption postOption) {
        // Validate required fields
        if (postOption.getTitle() == null || postOption.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        // Set default times if null
        LocalDateTime now = LocalDateTime.now();
        if (postOption.getStartTime() == null) {
            postOption.setStartTime(now);
        }
        if (postOption.getEndTime() == null) {
            postOption.setEndTime(now.plusDays(7));
        }

        // Validate times
        if (postOption.getStartTime().isAfter(postOption.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        // Ensure createdBy is set
        if (postOption.getCreatedBy() == null) {
            throw new IllegalArgumentException("Auction creator is required");
        }

        return repository.save(postOption);
    }

    public void deleteAuction(Long id) {
        repository.deleteById(id);
    }
}
