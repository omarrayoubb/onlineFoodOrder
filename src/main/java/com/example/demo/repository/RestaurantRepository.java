package com.example.demo.repository;

import com.example.demo.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // Find restaurants with a good rating
    List<Restaurant> findByAvgRatingGreaterThanEqual(Double rating);

    // Search by name (Case insensitive)
    List<Restaurant> findByNameContainingIgnoreCase(String name);
}