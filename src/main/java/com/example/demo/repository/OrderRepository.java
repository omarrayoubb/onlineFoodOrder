package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Customer History
    List<Order> findByCustomerId(Long customerId);

    // Restaurant Dashboard (Incoming orders)
    List<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status);

    // Driver Dashboard (Active deliveries)
    List<Order> findByDeliveryStaffIdAndStatus(Long deliveryStaffId, OrderStatus status);

    // Admin / Analytics: Find all active orders
    List<Order> findByStatusIn(List<OrderStatus> statuses);
}