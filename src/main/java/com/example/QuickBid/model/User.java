package com.example.QuickBid.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false, length = 50)
    private String fullname;

    @Column(nullable = false, length = 50)
    private String address;

    @Column(nullable = false, length = 10)
    private String contact;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "whoApproved", nullable = false)
    private Admin admin;

    public User(int userId, String fullname, String address, String contact, String username, String email, String password, LocalDateTime createdAt, Admin admin) {
        this.userId = userId;
        this.fullname = fullname;
        this.address = address;
        this.contact = contact;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.admin = admin;
    }
}