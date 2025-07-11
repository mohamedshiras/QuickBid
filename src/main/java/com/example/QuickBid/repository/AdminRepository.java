package com.example.QuickBid.repository;

import com.example.QuickBid.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    @Query("SELECT a FROM Admin a WHERE a.adminUsername = :username")
    Optional<Admin> findByUsername(@Param("username") String username);

    Optional<Admin> findByAdminUsername(String username);

}
