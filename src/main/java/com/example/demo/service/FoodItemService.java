package com.example.demo.service;

import com.example.demo.entity.FoodItem;
import com.example.demo.entity.Category;
import com.example.demo.repository.FoodItemRepository;
import com.example.demo.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;
    private final CategoryRepository categoryRepository;

    public FoodItemService(FoodItemRepository foodItemRepository, CategoryRepository categoryRepository) {
        this.foodItemRepository = foodItemRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public FoodItem createFoodItem(Long categoryId, String name, Double basePrice,
            Map<String, Double> availableAdditions) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        FoodItem foodItem = new FoodItem();
        foodItem.setName(name);
        foodItem.setBasePrice(basePrice);
        foodItem.setCategory(category);
        foodItem.setAvailableAdditions(availableAdditions);

        // Maintain bidirectional relationship: add food item to category's list
        category.getFoodItems().add(foodItem);

        return foodItemRepository.save(foodItem);
    }

    public FoodItem getFoodItemById(Long foodItemId) {
        return foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new IllegalArgumentException("Food item not found"));
    }

    public List<FoodItem> getFoodItemsByCategory(Long categoryId) {
        return foodItemRepository.findByCategoryId(categoryId);
    }

    public List<FoodItem> getFoodItemsByRestaurant(Long restaurantId) {
        // Get all categories for the restaurant (including subcategories), then get
        // food items for each
        List<Category> categories = categoryRepository.findAllByRestaurantIdWithFoodItems(restaurantId);
        return categories.stream()
                .flatMap(category -> category.getFoodItems().stream())
                .toList();
    }

    @Transactional
    public FoodItem updateFoodItem(Long foodItemId, Long restaurantId, String name, Double basePrice,
            Map<String, Double> availableAdditions) {
        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new IllegalArgumentException("Food item not found"));

        // Validate ownership through category
        if (!foodItem.getCategory().getRestaurant().getId().equals(restaurantId)) {
            throw new IllegalArgumentException("Food item does not belong to the specified restaurant");
        }

        foodItem.setName(name);
        foodItem.setBasePrice(basePrice);
        foodItem.setAvailableAdditions(availableAdditions);

        return foodItemRepository.save(foodItem);
    }

    @Transactional
    public void deleteFoodItem(Long foodItemId, Long restaurantId) {
        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new IllegalArgumentException("Food item not found"));

        // Validate ownership through category
        if (!foodItem.getCategory().getRestaurant().getId().equals(restaurantId)) {
            throw new IllegalArgumentException("Food item does not belong to the specified restaurant");
        }

        foodItemRepository.delete(foodItem);
    }
}
