package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.DashboardData;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.enums.UserRole;
import com.example.demo.factory.AuthenticationServiceFactory;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationServiceFactory serviceFactory;
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationController(AuthenticationServiceFactory serviceFactory, 
                                   UserRepository userRepository) {
        this.serviceFactory = serviceFactory;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthenticationService service = serviceFactory.getService(request.getUserRole());
            AuthResponse response = service.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AuthResponse(null, null, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AuthResponse(null, null, "Registration failed: " + e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            // First, find the user to determine their role
            User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

            // Get the appropriate service based on user role
            AuthenticationService service = serviceFactory.getService(user.getUserRole());
            AuthResponse response = service.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse(null, null, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AuthResponse(null, null, "Login failed: " + e.getMessage(), null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, null, "Invalid token format", null));
            }

            String jwtToken = token.substring(7); // Remove "Bearer " prefix
            
            // Get user role from token to determine which service to use
            // For simplicity, we'll use a default service (logout is same for all)
            AuthenticationService service = serviceFactory.getService(UserRole.CUSTOMER);
            service.logout(jwtToken);
            
            return ResponseEntity.ok(new AuthResponse(null, null, "Logout successful", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AuthResponse(null, null, "Logout failed: " + e.getMessage(), null));
        }
    }

    @GetMapping("/dashboard/{userId}")
    public ResponseEntity<?> getDashboardData(@PathVariable Long userId) {
        try {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

            AuthenticationService service = serviceFactory.getService(user.getUserRole());
            DashboardData dashboardData = service.getDashboardData(userId);
            return ResponseEntity.ok(dashboardData);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new DashboardData());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new DashboardData());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserData(@PathVariable Long userId) {
        try {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

            AuthenticationService service = serviceFactory.getService(user.getUserRole());
            DashboardData userData = service.getUserData(userId);
            return ResponseEntity.ok(userData);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new DashboardData());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new DashboardData());
        }
    }
}

