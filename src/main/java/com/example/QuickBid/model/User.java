package com.example.QuickBid.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserId;

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


    private String resetPasswordToken;

    private LocalDateTime resetPasswordTokenExpiry;


    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "who_approved", nullable = false)
    private Long whoApproved;

    // Constructors
    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public User(String fullname, String address, String contact, String username,
                String email, String password, Long whoApproved) {
        this.fullname = fullname;
        this.address = address;
        this.contact = contact;
        this.username = username;
        this.email = email;
        this.password = password;
        this.whoApproved = whoApproved;
        this.createdAt = LocalDateTime.now();
    }


}