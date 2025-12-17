package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    // --- COMPOSITE PATTERN: Self Reference ---
    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<Category> subCategories = new ArrayList<>();
    // ------------------------------------------

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<FoodItem> foodItems = new ArrayList<>();

    // --- CONSTRUCTORS ---
    public Category() {}

    public Category(String name) { this.name = name; }

    // --- GETTERS & SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

    public Category getParentCategory() { return parentCategory; }
    public void setParentCategory(Category parentCategory) { this.parentCategory = parentCategory; }

    public List<Category> getSubCategories() { return subCategories; }
    public void setSubCategories(List<Category> subCategories) { this.subCategories = subCategories; }

    public List<FoodItem> getFoodItems() { return foodItems; }
    public void setFoodItems(List<FoodItem> foodItems) { this.foodItems = foodItems; }

    // Helper for Composite Logic
    public void addSubCategory(Category child) {
        subCategories.add(child);
        child.setParentCategory(this);
    }
}