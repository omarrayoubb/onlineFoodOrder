package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "deliveries")
@PrimaryKeyJoinColumn(name = "user_id") // Links back to User.ID
public class Delivery extends User {

    // --- CONSTRUCTORS ---
    public Delivery() {
        super();
    }

    public Delivery(String email, String passwordHash, String name) {
        super(email, passwordHash, name, com.example.demo.enums.UserRole.DELIVERY);
    }
}

