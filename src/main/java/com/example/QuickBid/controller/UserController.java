package com.example.QuickBid.controller;

import com.example.QuickBid.model.User;
import com.example.QuickBid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// UserController.java
@RestController
@RequestMapping("/api/admin/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String status) {
        if (status != null) {
            return ResponseEntity.ok(userService.getUsersByStatus(status));
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{userId}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Integer userId, @RequestBody Map<String, String> request) {
        userService.updateUserStatus(userId, request.get("status"));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId) {
        userService.DeleteUser(userId);
        return ResponseEntity.ok().build();
    }
}