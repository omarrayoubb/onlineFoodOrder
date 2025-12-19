package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Map;

public class FoodItemRequest {

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotBlank(message = "Food item name is required")
    private String name;

    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private Double basePrice;

    private Map<String, Double> availableAdditions;

    public FoodItemRequest() {
    }

    public FoodItemRequest(Long categoryId, String name, Double basePrice, Map<String, Double> availableAdditions) {
        this.categoryId = categoryId;
        this.name = name;
        this.basePrice = basePrice;
        this.availableAdditions = availableAdditions;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Map<String, Double> getAvailableAdditions() {
        return availableAdditions;
    }

    public void setAvailableAdditions(Map<String, Double> availableAdditions) {
        this.availableAdditions = availableAdditions;
    }
}

