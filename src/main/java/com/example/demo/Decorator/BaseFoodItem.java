package com.example.demo.Decorator;

import com.example.demo.entity.FoodItem;

/**
 * Concrete component representing the base food item without any additions.
 * This is the core object that will be decorated.
 */
public class BaseFoodItem implements FoodItemComponent {
    
    private final FoodItem foodItem;
    
    public BaseFoodItem(FoodItem foodItem) {
        if (foodItem == null) {
            throw new IllegalArgumentException("FoodItem cannot be null");
        }
        this.foodItem = foodItem;
    }
    
    @Override
    public String getName() {
        return foodItem.getName();
    }
    
    @Override
    public Double getPrice() {
        return foodItem.getBasePrice() != null ? foodItem.getBasePrice() : 0.0;
    }
    
    @Override
    public String getDescription() {
        return foodItem.getName();
    }
    
    @Override
    public Double getBasePrice() {
        return foodItem.getBasePrice() != null ? foodItem.getBasePrice() : 0.0;
    }
    
    public FoodItem getFoodItem() {
        return foodItem;
    }
}

