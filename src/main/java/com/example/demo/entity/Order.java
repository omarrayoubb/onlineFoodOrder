package com.example.demo.entity;

import jakarta.persistence.*;
import com.example.demo.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private User deliveryStaff; // Refers to a User with DELIVERY role

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address deliveryAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Double totalPrice;
    private LocalDateTime createdAt;

    // Feedback
    private Integer feedbackStars;
    private String feedbackComment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    // --- PRIVATE CONSTRUCTOR (Forces usage of Builder) ---
    private Order(Builder builder) {
        this.customer = builder.customer;
        this.restaurant = builder.restaurant;
        this.deliveryStaff = builder.deliveryStaff;
        this.deliveryAddress = builder.deliveryAddress;
        this.status = builder.status;
        this.totalPrice = builder.totalPrice;
        this.items = builder.items;
        this.createdAt = LocalDateTime.now();

        // Link bidirectional relationship
        for(OrderItem item : this.items) {
            item.setOrder(this);
        }
    }

    // Required by JPA
    public Order() {}

    // --- THE MANUAL BUILDER PATTERN ---
    public static class Builder {
        private User customer;
        private Restaurant restaurant;
        private User deliveryStaff;
        private Address deliveryAddress;
        private OrderStatus status = OrderStatus.PLACED;
        private Double totalPrice = 0.0;
        private List<OrderItem> items = new ArrayList<>();

        public Builder forCustomer(User customer) {
            this.customer = customer;
            return this;
        }

        public Builder fromRestaurant(Restaurant restaurant) {
            this.restaurant = restaurant;
            return this;
        }

        public Builder toAddress(Address address) {
            this.deliveryAddress = address;
            return this;
        }

        public Builder addItem(OrderItem item) {
            this.items.add(item);
            if (item.getCalculatedPrice() != null) {
                this.totalPrice += item.getCalculatedPrice();
            }
            return this;
        }

        public Order build() {
            // Validation Logic
            if (customer == null) throw new IllegalStateException("Customer cannot be null");
            if (restaurant == null) throw new IllegalStateException("Restaurant cannot be null");
            if (items.isEmpty()) throw new IllegalStateException("Order must contain items");

            return new Order(this);
        }
    }

    // --- GETTERS & SETTERS (Standard) ---
    public Long getId() { return id; }
    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }
    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }
    public User getDeliveryStaff() { return deliveryStaff; }
    public void setDeliveryStaff(User deliveryStaff) { this.deliveryStaff = deliveryStaff; }
    public Address getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(Address deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public Integer getFeedbackStars() { return feedbackStars; }
    public void setFeedbackStars(Integer feedbackStars) { this.feedbackStars = feedbackStars; }
    public String getFeedbackComment() { return feedbackComment; }
    public void setFeedbackComment(String feedbackComment) { this.feedbackComment = feedbackComment; }
}