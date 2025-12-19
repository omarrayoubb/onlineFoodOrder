package com.example.demo.service;

import com.example.demo.builder.OrderBuilder;
import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.PaymentInfo;
import com.example.demo.entity.*;
import com.example.demo.factory.PaymentStrategyFactory;
import com.example.demo.repository.FoodItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.strategy.PaymentStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for handling order operations using the Decorator pattern for price
 * calculations.
 */
@Service("realOrderService")
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final FoodItemRepository foodItemRepository;
	private final PriceCalculationService priceCalculationService;
	private final PaymentStrategyFactory paymentStrategyFactory;

	public OrderServiceImpl(OrderRepository orderRepository,
			FoodItemRepository foodItemRepository,
			PriceCalculationService priceCalculationService,
			PaymentStrategyFactory paymentStrategyFactory) {
		this.orderRepository = orderRepository;
		this.foodItemRepository = foodItemRepository;
		this.priceCalculationService = priceCalculationService;
		this.paymentStrategyFactory = paymentStrategyFactory;
	}

	/**
	 * Creates an OrderItem with calculated price using the Decorator pattern.
	 *
	 * @param foodItemId        The ID of the food item
	 * @param quantity          The quantity of this item
	 * @param selectedAdditions List of addition names selected by the customer
	 * @return A new OrderItem with calculated price (unitPrice * quantity)
	 * @throws IllegalArgumentException if food item not found or additions are
	 *                                  invalid
	 */
	public OrderItem createOrderItem(Long foodItemId, Integer quantity, List<String> selectedAdditions) {
		FoodItem foodItem = foodItemRepository.findById(foodItemId)
				.orElseThrow(() -> new IllegalArgumentException("Food item not found with id: " + foodItemId));

		// Validate additions
		if (!priceCalculationService.validateAdditions(foodItem, selectedAdditions)) {
			throw new IllegalArgumentException("One or more selected additions are not available for this food item");
		}

		// Calculate unit price using decorator pattern (base price + additions)
		Double unitPrice = priceCalculationService.calculatePrice(foodItem, selectedAdditions);

		// Calculate total price for this item (unitPrice * quantity)
		Integer itemQuantity = quantity != null && quantity > 0 ? quantity : 1;
		Double calculatedPrice = unitPrice * itemQuantity;

		// Create and return OrderItem with quantity
		return new OrderItem(foodItem, selectedAdditions, itemQuantity, calculatedPrice);
	}

	/**
	 * Creates an order with multiple items.
	 *
	 * @param customer        The customer placing the order
	 * @param restaurant      The restaurant the order is from
	 * @param deliveryAddress The delivery address
	 * @param orderItems      List of order items (each with foodItemId, quantity,
	 *                        and selectedAdditions)
	 * @param paymentInfo     Payment information (method and details)
	 * @param notes           Optional notes for the order
	 * @return The created order
	 */
	@Transactional
	public Order createOrder(User customer, Restaurant restaurant, Address deliveryAddress,
			List<CreateOrderRequest.OrderItemRequest> orderItems, PaymentInfo paymentInfo, String notes) {
		if (orderItems == null || orderItems.isEmpty()) {
			throw new IllegalArgumentException("Order must contain at least one item");
		}

		if (paymentInfo == null) {
			throw new IllegalArgumentException("Payment information is required");
		}

		// Get appropriate payment strategy based on payment method
		PaymentStrategy paymentStrategy = paymentStrategyFactory.getStrategy(paymentInfo.getPaymentMethod());

		// Get shipping price from restaurant (default to 0.0 if not set)
		Double shippingPrice = restaurant.getShippingPrice() != null ? restaurant.getShippingPrice() : 0.0;

		// Create OrderItems using decorator pattern with quantities
		List<OrderItem> items = new ArrayList<>();
		for (CreateOrderRequest.OrderItemRequest request : orderItems) {
			OrderItem item = createOrderItem(
					request.getFoodItemId(),
					request.getQuantity(),
					request.getSelectedAdditions());
			items.add(item);
		}

		// Create a temporary order object for payment processing
		Order tempOrder = new Order();
		tempOrder.setTotalPrice(items.stream()
				.filter(item -> item.getCalculatedPrice() != null)
				.mapToDouble(OrderItem::getCalculatedPrice)
				.sum() + shippingPrice);

		// Process payment using strategy pattern
		boolean paymentProcessed = paymentStrategy.processPayment(tempOrder, paymentInfo);
		if (!paymentProcessed) {
			throw new IllegalArgumentException("Payment processing failed");
		}

		// Build order using Builder pattern: set fields → set items → set shipping →
		// set payment → set notes → build
		OrderBuilder builder = new OrderBuilder()
				.forCustomer(customer)
				.fromRestaurant(restaurant)
				.toAddress(deliveryAddress)
				.withItems(items) // Pass all items at once
				.withShippingPrice(shippingPrice) // Set shipping price
				.withPaymentInfo(paymentInfo) // Set payment information
				.withNotes(notes); // Set optional notes

		// Build the order (Builder calculates total: itemsTotal + shippingPrice)
		Order order = builder.build();

		return orderRepository.save(order);
	}
}
