package com.example.demo.Decorator;

/**
 * Base interface for the Decorator pattern.
 * Represents a food item that can be decorated with additions.
 */
public interface FoodItemComponent {
    
    /**
     * Gets the name of the food item (including additions)
     * @return The full name with additions
     */
    String getName();
    
    /**
     * Gets the total price including base price and all additions
     * @return The total price
     */
    Double getPrice();
    
    /**
     * Gets the description of the food item with all additions
     * @return The full description
     */
    String getDescription();
    
    /**
     * Gets the base price of the food item (without additions)
     * @return The base price
     */
    Double getBasePrice();
}

