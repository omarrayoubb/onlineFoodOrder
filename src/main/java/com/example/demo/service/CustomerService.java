package com.example.demo.service;

import com.example.demo.dto.DashboardData;
import com.example.demo.entity.User;
import com.example.demo.entity.Order;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.util.PasswordUtil;
import com.example.demo.auth.JwtTokenUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService extends AuthenticationService {

    private final OrderRepository orderRepository;

    public CustomerService(UserRepository userRepository, PasswordUtil passwordUtil, 
                          JwtTokenUtil jwtTokenUtil, OrderRepository orderRepository) {
        super(userRepository, passwordUtil, jwtTokenUtil);
        this.orderRepository = orderRepository;
    }

    @Override
    public DashboardData getDashboardData(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Get all orders made by this customer
        List<Order> orders = orderRepository.findByCustomerId(userId);

        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("userName", user.getName());
        data.put("userEmail", user.getEmail());
        data.put("orders", orders);
        data.put("totalOrders", orders.size());

        return new DashboardData(data);
    }

    @Override
    public DashboardData getUserData(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Get order history
        List<Order> orderHistory = orderRepository.findByCustomerId(userId);

        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("email", user.getEmail());
        data.put("name", user.getName());
        data.put("phone", user.getPhone());
        data.put("userRole", user.getUserRole());
        data.put("addresses", user.getAddresses());
        data.put("orderHistory", orderHistory);
        data.put("totalOrders", orderHistory.size());

        return new DashboardData(data);
    }
}

