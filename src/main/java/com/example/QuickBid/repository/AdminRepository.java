package com.example.QuickBid.repository;

import com.example.QuickBid.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByAdminUsername(String adminUsername);

    boolean existsByAdminUsername(String adminUsername);
}