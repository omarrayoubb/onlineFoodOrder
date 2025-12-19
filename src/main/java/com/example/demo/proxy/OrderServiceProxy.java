package com.example.demo.proxy;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.PaymentInfo;
import com.example.demo.entity.*;
import com.example.demo.enums.UserRole;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple Proxy that adds security checks and logging to OrderService
 */
@Service("orderService")
public class OrderServiceProxy implements OrderService {

	private final OrderService realOrderService;

	// Simple rate limiting: customerId -> count of orders in last hour
	private final Map<Long, Integer> orderCountCache = new HashMap<>();

	@Autowired
	public OrderServiceProxy(@Qualifier("realOrderService") OrderService realOrderService) {
		this.realOrderService = realOrderService;
	}

	@Override
	public Order createOrder(User customer, Restaurant restaurant, Address deliveryAddress,
			List<CreateOrderRequest.OrderItemRequest> orderItems,
			PaymentInfo paymentInfo, String notes) {

		System.out.println("=== Proxy: Order Creation Request ===");

		checkAuthentication(customer);
		validateInput(customer, restaurant, deliveryAddress, orderItems, paymentInfo);
		checkRateLimit(customer.getId());
		logRequest(customer, restaurant, orderItems);

		Order order = realOrderService.createOrder(customer, restaurant, deliveryAddress,
				orderItems, paymentInfo, notes);

		updateRateLimit(customer.getId());

		logSuccess(order);

		return order;
	}

	private void checkAuthentication(User customer) {
		System.out.println("Proxy: Checking authentication...");

		if (customer == null) {
			throw new SecurityException("Customer not authenticated");
		}

		// Only customers can place orders
		if (customer.getUserRole() != UserRole.CUSTOMER) {
			throw new SecurityException("Only customers can place orders. User role: " +
					customer.getUserRole());
		}

		System.out.println("Proxy: Authentication OK - Customer ID: " + customer.getId());
	}

	private void validateInput(User customer, Restaurant restaurant, Address deliveryAddress,
			List<CreateOrderRequest.OrderItemRequest> orderItems,
			PaymentInfo paymentInfo) {
		System.out.println("Proxy: Validating input...");

		if (restaurant == null) {
			throw new IllegalArgumentException("Restaurant is required");
		}

		if (deliveryAddress == null) {
			throw new IllegalArgumentException("Delivery address is required");
		}

		if (orderItems == null || orderItems.isEmpty()) {
			throw new IllegalArgumentException("Order must contain at least one item");
		}

		if (orderItems.size() > 50) {
			throw new SecurityException("Order cannot contain more than 50 items");
		}

		// Check each item quantity
		for (CreateOrderRequest.OrderItemRequest item : orderItems) {
			if (item.getQuantity() != null && item.getQuantity() > 10) {
				throw new SecurityException("Maximum quantity per item is 10");
			}
		}

		System.out.println("Proxy: Input validation OK");
	}

	private void checkRateLimit(Long customerId) {
		System.out.println("Proxy: Checking rate limit...");
		int maxOrders = 5;
		Integer currentCount = orderCountCache.get(customerId);

		if (currentCount != null && currentCount >= maxOrders) {
			throw new SecurityException("Rate limit exceeded. Maximum " + maxOrders +
					" orders allowed. Please wait.");
		}

		System.out.println("Proxy: Rate limit OK - Customer " + customerId +
				" has " + (currentCount != null ? currentCount : 0) + " orders");
	}

	private void updateRateLimit(Long customerId) {
		int currentCount = orderCountCache.getOrDefault(customerId, 0);
		orderCountCache.put(customerId, currentCount + 1);

		System.out.println("Proxy: Updated rate limit - Customer " + customerId +
				" now has " + (currentCount + 1) + " orders");
	}

	private void logRequest(User customer, Restaurant restaurant,
			List<CreateOrderRequest.OrderItemRequest> orderItems) {
		String logMessage = String.format(
				"Proxy Log: Customer %s (ID: %d) placing order at %s (ID: %d) with %d items at %s",
				customer.getEmail(),
				customer.getId(),
				restaurant.getName(),
				restaurant.getId(),
				orderItems.size(),
				LocalDateTime.now());

		System.out.println(logMessage);
	}

	private void logSuccess(Order order) {
		String logMessage = String.format(
				"Proxy Log: Order #%d created successfully. Total: $%.2f at %s",
				order.getId(),
				order.getTotalPrice(),
				LocalDateTime.now());

		System.out.println(logMessage);
		System.out.println("=== Proxy: Order Creation Complete ===\n");
	}

	/**
	 * Helper method to reset rate limits (for testing)
	 */
	public void resetRateLimits() {
		orderCountCache.clear();
		System.out.println("Proxy: Rate limits reset");
	}

	/**
	 * Helper method to get current rate limit counts
	 */
	public Map<Long, Integer> getRateLimitCounts() {
		return new HashMap<>(orderCountCache);
	}
}