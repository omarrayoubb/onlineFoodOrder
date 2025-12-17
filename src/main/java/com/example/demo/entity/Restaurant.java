package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
@PrimaryKeyJoinColumn(name = "user_id") // Links back to User.ID
public class Restaurant extends User {

    private String description;
    private Double avgRating;
    private String deliveryTimeEst;

    // Directly links to Categories (No Menu Table)
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();

    // --- CONSTRUCTORS ---
    public Restaurant() {}

    // --- GETTERS & SETTERS ---
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getAvgRating() { return avgRating; }
    public void setAvgRating(Double avgRating) { this.avgRating = avgRating; }

    public String getDeliveryTimeEst() { return deliveryTimeEst; }
    public void setDeliveryTimeEst(String deliveryTimeEst) { this.deliveryTimeEst = deliveryTimeEst; }

    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> categories) { this.categories = categories; }
}