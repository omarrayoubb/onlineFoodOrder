package com.example.demo.controller;

import com.example.demo.dto.FoodItemRequest;
import com.example.demo.entity.FoodItem;
import com.example.demo.service.FoodItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/food-items")
public class FoodItemController {

    private final FoodItemService foodItemService;

    @Autowired
    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @PostMapping
    public ResponseEntity<?> createFoodItem(@Valid @RequestBody FoodItemRequest request) {
        try {
            FoodItem foodItem = foodItemService.createFoodItem(
                    request.getCategoryId(),
                    request.getName(),
                    request.getBasePrice(),
                    request.getAvailableAdditions());
            return ResponseEntity.status(HttpStatus.CREATED).body(foodItem);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create food item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{foodItemId}")
    public ResponseEntity<?> getFoodItem(@PathVariable Long foodItemId) {
        try {
            FoodItem foodItem = foodItemService.getFoodItemById(foodItemId);
            return ResponseEntity.ok(foodItem);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to get food item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getFoodItemsByCategory(@PathVariable Long categoryId) {
        try {
            List<FoodItem> foodItems = foodItemService.getFoodItemsByCategory(categoryId);
            return ResponseEntity.ok(foodItems);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to get food items: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<?> getFoodItemsByRestaurant(@PathVariable Long restaurantId) {
        try {
            List<FoodItem> foodItems = foodItemService.getFoodItemsByRestaurant(restaurantId);
            return ResponseEntity.ok(foodItems);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to get food items: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{foodItemId}")
    public ResponseEntity<?> updateFoodItem(
            @PathVariable Long foodItemId,
            @RequestParam Long restaurantId,
            @Valid @RequestBody FoodItemRequest request) {
        try {
            FoodItem foodItem = foodItemService.updateFoodItem(
                    foodItemId,
                    restaurantId,
                    request.getName(),
                    request.getBasePrice(),
                    request.getAvailableAdditions());
            return ResponseEntity.ok(foodItem);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            HttpStatus status = e.getMessage().contains("not found") 
                    ? HttpStatus.NOT_FOUND 
                    : HttpStatus.FORBIDDEN;
            return ResponseEntity.status(status).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update food item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{foodItemId}")
    public ResponseEntity<?> deleteFoodItem(
            @PathVariable Long foodItemId,
            @RequestParam Long restaurantId) {
        try {
            foodItemService.deleteFoodItem(foodItemId, restaurantId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Food item deleted successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            HttpStatus status = e.getMessage().contains("not found") 
                    ? HttpStatus.NOT_FOUND 
                    : HttpStatus.FORBIDDEN;
            return ResponseEntity.status(status).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete food item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

