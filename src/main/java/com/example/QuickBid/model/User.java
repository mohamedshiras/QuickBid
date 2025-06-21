package com.example.QuickBid.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname", nullable = false, length = 50)
    private String fullname;

    @Column(name = "address", nullable = false, length = 50)
    private String address;

    @Column(name = "contact", nullable = false, length = 10)
    private String contact;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "createdAt", nullable = false)
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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getWhoApproved() {
        return whoApproved;
    }

    public void setWhoApproved(Long whoApproved) {
        this.whoApproved = whoApproved;
    }
}