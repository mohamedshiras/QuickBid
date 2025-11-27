package com.example.QuickBid.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int adminID;

    @Column(name = "admin_username") // matches database column
    private String adminUsername; // must match repository method naming

    @Column(name = "admin_password") // matches database column
    private String adminPassword;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
}