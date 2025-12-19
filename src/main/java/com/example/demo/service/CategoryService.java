package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Restaurant;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    public CategoryService(CategoryRepository categoryRepository, RestaurantRepository restaurantRepository) {
        this.categoryRepository = categoryRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Category createCategory(Long restaurantId, String name) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        Category category = new Category();
        category.setName(name);
        category.setRestaurant(restaurant);

        // Maintain bidirectional relationship: add category to restaurant's list
        restaurant.getCategories().add(category);

        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findByIdWithFoodItems(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    public List<Category> getCategoriesByRestaurant(Long restaurantId) {
        // Return categories with their food items eagerly loaded
        return categoryRepository.findByRestaurantIdWithFoodItems(restaurantId);
    }

    @Transactional
    public Category updateCategory(Long categoryId, Long restaurantId, String name) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // Validate ownership
        if (!category.getRestaurant().getId().equals(restaurantId)) {
            throw new IllegalArgumentException("Category does not belong to the specified restaurant");
        }

        category.setName(name);
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId, Long restaurantId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // Validate ownership
        if (!category.getRestaurant().getId().equals(restaurantId)) {
            throw new IllegalArgumentException("Category does not belong to the specified restaurant");
        }

        categoryRepository.delete(category);
    }
}
