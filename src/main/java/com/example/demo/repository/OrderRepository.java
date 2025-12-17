package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Customer History - using property path expression
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId")
    List<Order> findByCustomerId(@Param("customerId") Long customerId);

    // Restaurant Dashboard (Incoming orders) - using property path expression
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId AND o.status = :status")
    List<Order> findByRestaurantIdAndStatus(@Param("restaurantId") Long restaurantId, @Param("status") OrderStatus status);

    // Driver Dashboard (Active deliveries) - using property path expression
    @Query("SELECT o FROM Order o WHERE o.deliveryStaff.id = :deliveryStaffId AND o.status = :status")
    List<Order> findByDeliveryStaffIdAndStatus(@Param("deliveryStaffId") Long deliveryStaffId, @Param("status") OrderStatus status);

    // Admin / Analytics: Find all active orders
    List<Order> findByStatusIn(List<OrderStatus> statuses);
}