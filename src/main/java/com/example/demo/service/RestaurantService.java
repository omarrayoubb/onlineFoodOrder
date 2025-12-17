package com.example.demo.service;

import com.example.demo.dto.DashboardData;
import com.example.demo.entity.User;
import com.example.demo.entity.Restaurant;
import com.example.demo.entity.Order;
import com.example.demo.enums.OrderStatus;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.util.PasswordUtil;
import com.example.demo.auth.JwtTokenUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestaurantService extends AuthenticationService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(UserRepository userRepository, PasswordUtil passwordUtil,
                            JwtTokenUtil jwtTokenUtil, OrderRepository orderRepository,
                            RestaurantRepository restaurantRepository) {
        super(userRepository, passwordUtil, jwtTokenUtil);
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public DashboardData getDashboardData(Long userId) {
        Restaurant restaurant = restaurantRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        // Get incoming orders (not delivered or cancelled)
        List<Order> incomingOrders = orderRepository.findByRestaurantIdAndStatus(
            userId, 
            OrderStatus.PLACED
        );

        Map<String, Object> data = new HashMap<>();
        data.put("restaurantId", userId);
        data.put("restaurantName", restaurant.getName());
        data.put("description", restaurant.getDescription());
        data.put("avgRating", restaurant.getAvgRating());
        data.put("deliveryTimeEst", restaurant.getDeliveryTimeEst());
        data.put("incomingOrders", incomingOrders);
        data.put("totalIncomingOrders", incomingOrders.size());
        data.put("categories", restaurant.getCategories());

        return new DashboardData(data);
    }

    @Override
    public DashboardData getUserData(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("email", user.getEmail());
        data.put("name", restaurant.getName());
        data.put("phone", user.getPhone());
        data.put("userRole", user.getUserRole());
        data.put("description", restaurant.getDescription());
        data.put("avgRating", restaurant.getAvgRating());
        data.put("deliveryTimeEst", restaurant.getDeliveryTimeEst());
        data.put("categories", restaurant.getCategories());
        data.put("menu", restaurant.getCategories()); // Categories contain food items

        return new DashboardData(data);
    }
}

