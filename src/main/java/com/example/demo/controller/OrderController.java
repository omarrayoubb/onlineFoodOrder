package com.example.demo.controller;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public OrderController(OrderService orderService,
                          UserRepository userRepository,
                          RestaurantRepository restaurantRepository,
                          AddressRepository addressRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            // Get customer (assuming current user - you might want to get from JWT token)
            // For now, using a default customer ID. In production, get from authenticated user
            User customer = userRepository.findById(1L) // TODO: Get from JWT token or session
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

            // Get restaurant
            Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                    .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

            // Get address
            Address address = addressRepository.findById(request.getAddressId())
                    .orElseThrow(() -> new IllegalArgumentException("Address not found"));

            // Create order using decorator pattern and builder pattern
            Order order = orderService.createOrder(customer, restaurant, address, request.getItems());

            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

