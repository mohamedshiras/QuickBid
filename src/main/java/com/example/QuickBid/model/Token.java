package com.example.QuickBid.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserId;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiryDate;



    public boolean isExpired() {
        return expiryDate.isBefore(LocalDateTime.now());
    }

    // Getters and Setters

    public Long getId() {
        return UserId;
    }

    public void setId(Long id) {
        this.UserId = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
