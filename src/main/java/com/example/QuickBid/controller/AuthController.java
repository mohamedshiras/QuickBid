package com.example.QuickBid.controller;

import com.example.QuickBid.model.User;
import com.example.QuickBid.repository.UserRepository; // Import UserRepository
import com.example.QuickBid.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Base64; // Import Base64
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // NEW: Inject the UserRepository to fetch the user's details
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam("username") String usernameOrEmail,
            @RequestParam("password") String password,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            User authenticatedUser = authService.authenticateUser(usernameOrEmail, password).getUser();

            if (authenticatedUser != null) {
                // Store user information in session
                session.setAttribute("userId", authenticatedUser.getUserId());
                session.setAttribute("username", authenticatedUser.getUsername());
                session.setAttribute("fullname", authenticatedUser.getFullname());
                session.setAttribute("userType", "USER");

                response.put("success", true);
                response.put("message", "Login successful");
                response.put("userId", authenticatedUser.getUserId());
                response.put("username", authenticatedUser.getUsername());
                response.put("fullname", authenticatedUser.getFullname());

                // NEW: Add profile picture URL to the login response
                if (authenticatedUser.getProfilePicture() != null && authenticatedUser.getProfilePicture().length > 0) {
                    String profilePicUrl = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(authenticatedUser.getProfilePicture());
                    response.put("profilePicUrl", profilePicUrl);
                }

                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid username or password");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam("fullname") String fullname,
            @RequestParam("address") String address,
            @RequestParam("contact") String contact,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword) {

        Map<String, Object> response = new HashMap<>();

        try {
            // ... (rest of your registration logic is fine)
            String[] fields = { fullname, address, contact, username, email, password, confirmPassword };
            boolean hasEmptyField = Arrays.stream(fields)
                    .anyMatch(field -> field == null || field.trim().isEmpty());

            if (hasEmptyField) {
                response.put("success", false);
                response.put("message", "All fields are required");
                return ResponseEntity.badRequest().body(response);
            }
            if (!contact.matches("^[0-9]{10}$")) {
                response.put("success", false);
                response.put("message", "Contact number must be exactly 10 digits");
                return ResponseEntity.badRequest().body(response);
            }
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                response.put("success", false);
                response.put("message", "Please enter a valid email address");
                return ResponseEntity.badRequest().body(response);
            }
            if (!password.equals(confirmPassword)) {
                response.put("success", false);
                response.put("message", "Passwords do not match");
                return ResponseEntity.badRequest().body(response);
            }
            if (password.length() < 6) {
                response.put("success", false);
                response.put("message", "Password must be at least 6 characters long");
                return ResponseEntity.badRequest().body(response);
            }
            User newUser = authService.registerUser(
                    fullname.trim(),
                    address.trim(),
                    contact.trim(),
                    username.trim(),
                    email.trim().toLowerCase(),
                    password
            );
            response.put("success", true);
            response.put("message", "Registration successful");
            response.put("userId", newUser.getUserId());
            response.put("username", newUser.getUsername());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            session.invalidate();
            response.put("success", true);
            response.put("message", "Logout successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Logout failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/check-session")
    public ResponseEntity<Map<String, Object>> checkSession(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId != null) {
            response.put("loggedIn", true);
            response.put("userId", userId);
            response.put("username", session.getAttribute("username"));
            response.put("fullname", session.getAttribute("fullname"));

            // NEW: Fetch the user from the database to get the profile picture
            userRepository.findById(userId).ifPresent(user -> {
                if (user.getProfilePicture() != null && user.getProfilePicture().length > 0) {
                    // Encode the byte array to a Base64 string and create a data URL
                    String profilePicUrl = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(user.getProfilePicture());
                    response.put("profilePicUrl", profilePicUrl);
                }
            });

        } else {
            response.put("loggedIn", false);
        }

        return ResponseEntity.ok(response);
    }
}
