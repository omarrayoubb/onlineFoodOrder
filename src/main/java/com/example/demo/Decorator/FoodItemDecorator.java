package com.example.demo.Decorator;

/**
 * Abstract decorator class that implements FoodItemComponent.
 * All concrete decorators will extend this class.
 */
public abstract class FoodItemDecorator implements FoodItemComponent {
    
    protected FoodItemComponent component;
    
    public FoodItemDecorator(FoodItemComponent component) {
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null");
        }
        this.component = component;
    }
    
    @Override
    public String getName() {
        return component.getName();
    }
    
    @Override
    public Double getPrice() {
        return component.getPrice();
    }
    
    @Override
    public String getDescription() {
        return component.getDescription();
    }
    
    @Override
    public Double getBasePrice() {
        return component.getBasePrice();
    }
}

