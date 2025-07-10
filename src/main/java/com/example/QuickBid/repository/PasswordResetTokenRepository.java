package com.example.QuickBid.repository;

import com.example.QuickBid.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByEmailAndToken(String email, String token);
}
