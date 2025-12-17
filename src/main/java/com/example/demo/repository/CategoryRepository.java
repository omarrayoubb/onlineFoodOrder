package com.example.demo.repository;

import com.example.demo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Find all Root Categories for a specific restaurant
    // (Where parentCategory is NULL) - This is the starting point of your Tree
    List<Category> findByRestaurantIdAndParentCategoryIsNull(Long restaurantId);

    // Find sub-categories of a specific folder
    List<Category> findByParentCategoryId(Long parentId);
}