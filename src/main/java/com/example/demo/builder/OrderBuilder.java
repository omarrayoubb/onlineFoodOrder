package com.example.demo.builder;

import com.example.demo.entity.*;
import com.example.demo.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for constructing Order objects using the Builder pattern.
 * This separates the construction logic from the Order entity.
 */
public class OrderBuilder {
    private User customer;
    private Restaurant restaurant;
    private User deliveryStaff;
    private Address deliveryAddress;
    private OrderStatus status = OrderStatus.PLACED;
    private Double shippingPrice = 0.0;
    private Double totalPrice = 0.0;
    private List<OrderItem> items = new ArrayList<>();

    public OrderBuilder forCustomer(User customer) {
        this.customer = customer;
        return this;
    }

    public OrderBuilder fromRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    public OrderBuilder toAddress(Address address) {
        this.deliveryAddress = address;
        return this;
    }

    public OrderBuilder withDeliveryStaff(User deliveryStaff) {
        this.deliveryStaff = deliveryStaff;
        return this;
    }

    public OrderBuilder withStatus(OrderStatus status) {
        this.status = status != null ? status : OrderStatus.PLACED;
        return this;
    }

    public OrderBuilder addItem(OrderItem item) {
        this.items.add(item);
        if (item.getCalculatedPrice() != null) {
            // calculatedPrice already includes quantity multiplication
            this.totalPrice += item.getCalculatedPrice();
        }
        return this;
    }

    public OrderBuilder withItems(List<OrderItem> items) {
        if (items != null) {
            this.items = new ArrayList<>(items);
            // Calculate total price from all items
            this.totalPrice = items.stream()
                    .filter(item -> item.getCalculatedPrice() != null)
                    .mapToDouble(OrderItem::getCalculatedPrice)
                    .sum();
        }
        return this;
    }

    public OrderBuilder withShippingPrice(Double shippingPrice) {
        this.shippingPrice = shippingPrice != null ? shippingPrice : 0.0;
        return this;
    }

    public Order build() {
        // Validation Logic
        if (customer == null)
            throw new IllegalStateException("Customer cannot be null");
        if (restaurant == null)
            throw new IllegalStateException("Restaurant cannot be null");
        if (items.isEmpty())
            throw new IllegalStateException("Order must contain items");

        // Calculate total: items total + shipping price
        this.totalPrice = this.totalPrice + this.shippingPrice;

        // Create Order using package-private constructor
        Order order = new Order();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setDeliveryStaff(deliveryStaff);
        order.setDeliveryAddress(deliveryAddress);
        order.setStatus(status);
        order.setShippingPrice(shippingPrice);
        order.setTotalPrice(totalPrice);
        order.setItems(items);
        order.setCreatedAt(LocalDateTime.now());

        // Link bidirectional relationship
        for (OrderItem item : items) {
            item.setOrder(order);
        }

        return order;
    }
}

