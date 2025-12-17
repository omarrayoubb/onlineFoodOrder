package com.example.demo.service;

import com.example.demo.dto.DashboardData;
import com.example.demo.entity.User;
import com.example.demo.entity.Order;
import com.example.demo.enums.OrderStatus;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.util.PasswordUtil;
import com.example.demo.auth.JwtTokenUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeliveryService extends AuthenticationService {

    private final OrderRepository orderRepository;

    public DeliveryService(UserRepository userRepository, PasswordUtil passwordUtil,
                          JwtTokenUtil jwtTokenUtil, OrderRepository orderRepository) {
        super(userRepository, passwordUtil, jwtTokenUtil);
        this.orderRepository = orderRepository;
    }

    @Override
    public DashboardData getDashboardData(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Get active deliveries (not delivered or cancelled)
        List<Order> activeDeliveries = orderRepository.findByDeliveryStaffIdAndStatus(
            userId, 
            OrderStatus.OUT_FOR_DELIVERY
        );

        // Also get orders that are cooking (potential future deliveries)
        List<Order> cookingOrders = orderRepository.findByDeliveryStaffIdAndStatus(
            userId,
            OrderStatus.COOKING
        );

        Map<String, Object> data = new HashMap<>();
        data.put("deliveryStaffId", userId);
        data.put("deliveryStaffName", user.getName());
        data.put("activeDeliveries", activeDeliveries);
        data.put("cookingOrders", cookingOrders);
        data.put("totalActiveDeliveries", activeDeliveries.size());
        data.put("totalCookingOrders", cookingOrders.size());

        return new DashboardData(data);
    }

    @Override
    public DashboardData getUserData(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Get all orders delivered by this delivery staff
        List<Order> deliveredOrders = orderRepository.findByDeliveryStaffIdAndStatus(
            userId,
            OrderStatus.DELIVERED
        );

        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("email", user.getEmail());
        data.put("name", user.getName());
        data.put("phone", user.getPhone());
        data.put("userRole", user.getUserRole());
        data.put("deliveryHistory", deliveredOrders);
        data.put("totalDeliveries", deliveredOrders.size());

        return new DashboardData(data);
    }
}

