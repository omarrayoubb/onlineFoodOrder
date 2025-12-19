package com.example.demo.repository;

import com.example.demo.entity.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Find all Root Categories for a specific restaurant
    // (Where parentCategory is NULL) - This is the starting point of your Tree
    List<Category> findByRestaurantIdAndParentCategoryIsNull(Long restaurantId);

    // Find sub-categories of a specific folder
    List<Category> findByParentCategoryId(Long parentId);

    // Find all categories for a restaurant with food items eagerly loaded
    @EntityGraph(attributePaths = { "foodItems" })
    @Query("SELECT c FROM Category c WHERE c.restaurant.id = :restaurantId AND c.parentCategory IS NULL")
    List<Category> findByRestaurantIdWithFoodItems(@Param("restaurantId") Long restaurantId);

    // Find all categories for a restaurant (including subcategories) with food
    // items eagerly loaded
    @EntityGraph(attributePaths = { "foodItems" })
    @Query("SELECT c FROM Category c WHERE c.restaurant.id = :restaurantId")
    List<Category> findAllByRestaurantIdWithFoodItems(@Param("restaurantId") Long restaurantId);

    // Find a single category with food items eagerly loaded
    @EntityGraph(attributePaths = { "foodItems", "restaurant" })
    @Query("SELECT c FROM Category c WHERE c.id = :categoryId")
    java.util.Optional<Category> findByIdWithFoodItems(@Param("categoryId") Long categoryId);
}