package com.example.demo.repository;

import com.example.demo.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    // Get all food items in a specific category (e.g., all Burgers)
    List<FoodItem> findByCategoryId(Long categoryId);

    // Search for food globally (e.g., "Pizza")
    List<FoodItem> findByNameContainingIgnoreCase(String name);
}