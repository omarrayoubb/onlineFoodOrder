package com.example.demo.service;

import com.example.demo.dto.DashboardData;
import com.example.demo.entity.User;
import com.example.demo.entity.Restaurant;
import com.example.demo.entity.Order;
import com.example.demo.entity.Category;
import com.example.demo.enums.OrderStatus;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.repository.CategoryRepository;
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
        private final CategoryRepository categoryRepository;

        public RestaurantService(UserRepository userRepository, PasswordUtil passwordUtil,
                        JwtTokenUtil jwtTokenUtil, OrderRepository orderRepository,
                        RestaurantRepository restaurantRepository, CategoryRepository categoryRepository) {
                super(userRepository, passwordUtil, jwtTokenUtil);
                this.orderRepository = orderRepository;
                this.restaurantRepository = restaurantRepository;
                this.categoryRepository = categoryRepository;
        }

        @Override
        public DashboardData getDashboardData(Long userId) {
                // First fetch restaurant with categories (without food items to avoid
                // MultipleBagFetchException)
                Restaurant restaurant = restaurantRepository.findByIdWithCategories(userId)
                                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

                // Then fetch categories with their food items separately
                List<Category> categoriesWithFoodItems = categoryRepository.findByRestaurantIdWithFoodItems(userId);

                // Replace the categories in restaurant with the ones that have food items
                // loaded
                restaurant.setCategories(categoriesWithFoodItems);

                // Get incoming orders (not delivered or cancelled)
                List<Order> incomingOrders = orderRepository.findByRestaurantIdAndStatus(
                                userId,
                                OrderStatus.PLACED);

                Map<String, Object> data = new HashMap<>();
                data.put("restaurantId", userId);
                data.put("restaurantName", restaurant.getName());
                data.put("description", restaurant.getDescription());
                data.put("avgRating", restaurant.getAvgRating());
                data.put("deliveryTimeEst", restaurant.getDeliveryTimeEst());
                data.put("incomingOrders", incomingOrders);
                data.put("totalIncomingOrders", incomingOrders.size());

                // Return nested menu structure: categories with their food items
                data.put("categories", restaurant.getCategories());
                data.put("menu", restaurant.getCategories()); // Categories contain food items

                return new DashboardData(data);
        }

        @Override
        public DashboardData getUserData(Long userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));

                // First fetch restaurant with categories (without food items to avoid
                // MultipleBagFetchException)
                Restaurant restaurant = restaurantRepository.findByIdWithCategories(userId)
                                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

                // Then fetch categories with their food items separately
                List<Category> categoriesWithFoodItems = categoryRepository.findByRestaurantIdWithFoodItems(userId);

                // Replace the categories in restaurant with the ones that have food items
                // loaded
                restaurant.setCategories(categoriesWithFoodItems);

                Map<String, Object> data = new HashMap<>();
                data.put("id", user.getId());
                data.put("email", user.getEmail());
                data.put("name", restaurant.getName());
                data.put("phone", user.getPhone());
                data.put("userRole", user.getUserRole());
                data.put("description", restaurant.getDescription());
                data.put("avgRating", restaurant.getAvgRating());
                data.put("deliveryTimeEst", restaurant.getDeliveryTimeEst());
                // Return nested menu structure: categories with their food items
                data.put("categories", restaurant.getCategories());
                data.put("menu", restaurant.getCategories()); // Categories contain food items

                return new DashboardData(data);
        }
}
