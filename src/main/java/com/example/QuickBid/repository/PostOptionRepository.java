package com.example.QuickBid.repository;

import com.example.QuickBid.model.PostOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostOptionRepository extends JpaRepository<PostOption, Long> {

}
