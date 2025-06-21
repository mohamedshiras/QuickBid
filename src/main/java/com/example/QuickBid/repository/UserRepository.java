package com.example.QuickBid.repository;

import com.example.QuickBid.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username
    Optional<User> findByUsername(String username);

    // Find user by email
    Optional<User> findByEmail(String email);

    // Check if username exists
    boolean existsByUsername(String username);

    // Check if email exists
    boolean existsByEmail(String email);

    // Find user by username or email for login
    @Query("SELECT u FROM User u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    // Check if user exists by username or email
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    boolean existsByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);
}