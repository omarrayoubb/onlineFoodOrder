package com.example.demo.service;

import com.example.demo.Decorator.AdditionDecorator;
import com.example.demo.Decorator.BaseFoodItem;
import com.example.demo.Decorator.FoodItemComponent;
import com.example.demo.entity.FoodItem;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service for calculating food item prices using the Decorator pattern.
 * This service handles the creation of decorated food items and price calculations.
 */
@Service
public class PriceCalculationService {

    /**
     * Creates a decorated food item component with the selected additions.
     * 
     * @param foodItem The base food item
     * @param selectedAdditions List of addition names selected by the customer
     * @return A decorated FoodItemComponent with all additions applied
     * @throws IllegalArgumentException if addition is not available or invalid
     */
    public FoodItemComponent createDecoratedFoodItem(FoodItem foodItem, List<String> selectedAdditions) {
        if (foodItem == null) {
            throw new IllegalArgumentException("FoodItem cannot be null");
        }
        if (selectedAdditions == null) {
            selectedAdditions = List.of();
        }

        // Start with the base food item
        FoodItemComponent decorated = new BaseFoodItem(foodItem);

        // Get available additions from the food item
        Map<String, Double> availableAdditions = foodItem.getAvailableAdditions();
        
        if (availableAdditions == null || availableAdditions.isEmpty()) {
            // No additions available, return base item
            return decorated;
        }

        // Apply each selected addition as a decorator
        for (String additionName : selectedAdditions) {
            if (additionName == null || additionName.trim().isEmpty()) {
                continue; // Skip empty additions
            }

            // Check if the addition is available
            Double additionPrice = availableAdditions.get(additionName);
            if (additionPrice == null) {
                throw new IllegalArgumentException(
                    String.format("Addition '%s' is not available for food item '%s'", 
                        additionName, foodItem.getName()));
            }

            // Decorate with the addition
            decorated = new AdditionDecorator(decorated, additionName, additionPrice);
        }

        return decorated;
    }

    /**
     * Calculates the total price for a food item with selected additions.
     * 
     * @param foodItem The base food item
     * @param selectedAdditions List of addition names
     * @return The total price (base price + all additions)
     */
    public Double calculatePrice(FoodItem foodItem, List<String> selectedAdditions) {
        FoodItemComponent decorated = createDecoratedFoodItem(foodItem, selectedAdditions);
        return decorated.getPrice();
    }

    /**
     * Gets the description of a food item with all additions.
     * 
     * @param foodItem The base food item
     * @param selectedAdditions List of addition names
     * @return The full description including all additions
     */
    public String getDescription(FoodItem foodItem, List<String> selectedAdditions) {
        FoodItemComponent decorated = createDecoratedFoodItem(foodItem, selectedAdditions);
        return decorated.getDescription();
    }

    /**
     * Validates that all selected additions are available for the food item.
     * 
     * @param foodItem The base food item
     * @param selectedAdditions List of addition names to validate
     * @return true if all additions are valid, false otherwise
     */
    public boolean validateAdditions(FoodItem foodItem, List<String> selectedAdditions) {
        if (foodItem == null || selectedAdditions == null || selectedAdditions.isEmpty()) {
            return true; // Empty list is valid
        }

        Map<String, Double> availableAdditions = foodItem.getAvailableAdditions();
        if (availableAdditions == null || availableAdditions.isEmpty()) {
            return selectedAdditions.isEmpty(); // No additions available, so none should be selected
        }

        // Check if all selected additions exist in available additions
        for (String addition : selectedAdditions) {
            if (addition == null || addition.trim().isEmpty()) {
                continue;
            }
            if (!availableAdditions.containsKey(addition)) {
                return false;
            }
        }

        return true;
    }
}

