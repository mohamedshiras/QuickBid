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
    private Long adminID;

    @Column(nullable = false, unique = true, length = 50)
    private String adminUsername;

    @Column(nullable = false)
    private String adminPassword;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
}