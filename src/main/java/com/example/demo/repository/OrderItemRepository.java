package com.example.demo.repository;

import com.example.demo.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Usually not accessed directly, but useful for analytics
    // e.g., "How many times was a Burger ordered?"
    long countByFoodItemId(Long foodItemId);
}