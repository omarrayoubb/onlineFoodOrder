package com.example.demo.dto;

import com.example.demo.enums.UserRole;

public class AuthResponse {

    private String token;
    private UserRole userRole;
    private String message;
    private Long userId;

    public AuthResponse() {
    }

    public AuthResponse(String token, UserRole userRole, String message, Long userId) {
        this.token = token;
        this.userRole = userRole;
        this.message = message;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

