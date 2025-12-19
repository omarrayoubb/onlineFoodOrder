package com.example.demo.repository;

import com.example.demo.entity.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // Find restaurants with a good rating
    List<Restaurant> findByAvgRatingGreaterThanEqual(Double rating);

    // Search by name (Case insensitive)
    List<Restaurant> findByNameContainingIgnoreCase(String name);

    // Find restaurant with categories (without food items to avoid
    // MultipleBagFetchException)
    @EntityGraph(attributePaths = { "categories" })
    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.categories WHERE r.id = :restaurantId")
    Optional<Restaurant> findByIdWithCategories(@Param("restaurantId") Long restaurantId);
}