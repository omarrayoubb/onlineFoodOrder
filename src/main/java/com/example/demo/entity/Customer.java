package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "user_id") // Links back to User.ID
public class Customer extends User {

    // --- CONSTRUCTORS ---
    public Customer() {
        super();
    }

    public Customer(String email, String passwordHash, String name) {
        super(email, passwordHash, name, com.example.demo.enums.UserRole.CUSTOMER);
    }
}

