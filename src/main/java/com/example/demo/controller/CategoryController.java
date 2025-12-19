package com.example.demo.controller;

import com.example.demo.dto.CategoryRequest;
import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest request) {
        try {
            Category category = categoryService.createCategory(request.getRestaurantId(), request.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create category: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategory(@PathVariable Long categoryId) {
        try {
            Category category = categoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(category);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to get category: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<?> getCategoriesByRestaurant(@PathVariable Long restaurantId) {
        try {
            List<Category> categories = categoryService.getCategoriesByRestaurant(restaurantId);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to get categories: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequest request) {
        try {
            Category category = categoryService.updateCategory(
                    categoryId,
                    request.getRestaurantId(),
                    request.getName());
            return ResponseEntity.ok(category);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            HttpStatus status = e.getMessage().contains("not found") 
                    ? HttpStatus.NOT_FOUND 
                    : HttpStatus.FORBIDDEN;
            return ResponseEntity.status(status).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update category: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long categoryId,
            @RequestParam Long restaurantId) {
        try {
            categoryService.deleteCategory(categoryId, restaurantId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Category deleted successfully");
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
            error.put("error", "Failed to delete category: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

