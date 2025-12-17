package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.DashboardData;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.factory.UserFactory;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.PasswordUtil;
import com.example.demo.auth.JwtTokenUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public abstract class AuthenticationService {

    protected final UserRepository userRepository;
    protected final PasswordUtil passwordUtil;
    protected final JwtTokenUtil jwtTokenUtil;

    public AuthenticationService(UserRepository userRepository, PasswordUtil passwordUtil, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * Abstract method to get dashboard data - implemented by each role-specific service
     */
    public abstract DashboardData getDashboardData(Long userId);

    /**
     * Abstract method to get user data - implemented by each role-specific service
     */
    public abstract DashboardData getUserData(Long userId);

    /**
     * Register a new user
     */
    public AuthResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Hash the password
        String hashedPassword = passwordUtil.hashPassword(request.getPassword());

        // Create user using factory
        User user = UserFactory.createUser(
            request.getUserRole(),
            request.getEmail(),
            hashedPassword,
            request.getName()
        );

        // Set optional phone
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            user.setPhone(request.getPhone());
        }

        // Save user
        User savedUser = userRepository.save(user);

        // Generate JWT token
        UserDetails userDetails = createUserDetails(savedUser);
        String token = jwtTokenUtil.generateToken(userDetails);

        return new AuthResponse(token, savedUser.getUserRole(), "Registration successful", savedUser.getId());
    }

    /**
     * Login a user
     */
    public AuthResponse login(LoginRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        // Verify password
        if (!passwordUtil.verifyPassword(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid password");
        }

        // Generate JWT token
        UserDetails userDetails = createUserDetails(user);
        String token = jwtTokenUtil.generateToken(userDetails);

        return new AuthResponse(token, user.getUserRole(), "Login successful", user.getId());
    }

    /**
     * Logout a user (simple implementation - token invalidation can be enhanced later)
     */
    public void logout(String token) {
        // For now, logout is handled client-side by removing the token
        // In a production system, you might want to maintain a blacklist of tokens
        // or use a token store that supports revocation
    }

    /**
     * Helper method to convert User entity to UserDetails for JWT token generation
     */
    protected UserDetails createUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPasswordHash())
            .authorities(Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name())
            ))
            .build();
    }
}

