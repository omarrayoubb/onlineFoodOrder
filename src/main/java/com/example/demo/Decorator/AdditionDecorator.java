package com.example.demo.Decorator;

/**
 * Concrete decorator for adding additions (toppings, extras, etc.) to food items.
 * This is a flexible decorator that can represent any type of addition.
 */
public class AdditionDecorator extends FoodItemDecorator {
    
    private final String additionName;
    private final Double additionPrice;
    
    public AdditionDecorator(FoodItemComponent component, String additionName, Double additionPrice) {
        super(component);
        if (additionName == null || additionName.trim().isEmpty()) {
            throw new IllegalArgumentException("Addition name cannot be null or empty");
        }
        if (additionPrice == null || additionPrice < 0) {
            throw new IllegalArgumentException("Addition price cannot be null or negative");
        }
        this.additionName = additionName;
        this.additionPrice = additionPrice;
    }
    
    @Override
    public Double getPrice() {
        return component.getPrice() + additionPrice;
    }
    
    @Override
    public String getDescription() {
        return component.getDescription() + ", " + additionName;
    }
    
    public String getAdditionName() {
        return additionName;
    }
    
    public Double getAdditionPrice() {
        return additionPrice;
    }
}

