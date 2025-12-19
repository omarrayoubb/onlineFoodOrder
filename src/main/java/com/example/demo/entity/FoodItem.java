package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.Map;

@Entity
@Table(name = "food_items")
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double basePrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;

    // --- DECORATOR CONFIG ---
    // Example: {"Cheese": 2.50, "Bacon": 3.00}
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Double> availableAdditions;

    // --- CONSTRUCTORS ---
    public FoodItem() {}

    // --- GETTERS & SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getBasePrice() { return basePrice; }
    public void setBasePrice(Double basePrice) { this.basePrice = basePrice; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Map<String, Double> getAvailableAdditions() { return availableAdditions; }
    public void setAvailableAdditions(Map<String, Double> availableAdditions) {
        this.availableAdditions = availableAdditions;
    }
}