package com.example.demo.controller;

import com.example.demo.dto.DashboardData;
import com.example.demo.entity.Restaurant;
import com.example.demo.entity.Category;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public RestaurantController(RestaurantRepository restaurantRepository, CategoryRepository categoryRepository) {
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/{restaurantId}/menu")
    public ResponseEntity<?> getRestaurantMenu(@PathVariable Long restaurantId) {
        try {
            // First fetch restaurant with categories (without food items to avoid
            // MultipleBagFetchException)
            Restaurant restaurant = restaurantRepository.findByIdWithCategories(restaurantId)
                    .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

            // Then fetch categories with their food items separately
            List<Category> categoriesWithFoodItems = categoryRepository.findByRestaurantIdWithFoodItems(restaurantId);

            // Replace the categories in restaurant with the ones that have food items
            // loaded
            restaurant.setCategories(categoriesWithFoodItems);

            Map<String, Object> menuData = new HashMap<>();
            menuData.put("restaurantId", restaurant.getId());
            menuData.put("restaurantName", restaurant.getName());
            menuData.put("description", restaurant.getDescription());
            menuData.put("avgRating", restaurant.getAvgRating());
            menuData.put("deliveryTimeEst", restaurant.getDeliveryTimeEst());
            menuData.put("categories", restaurant.getCategories());

            return ResponseEntity.ok(new DashboardData(menuData));
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to get restaurant menu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
