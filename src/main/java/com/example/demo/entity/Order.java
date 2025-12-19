package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Double shippingPrice; // Shipping/delivery fee
    private LocalDateTime createdAt;

    // Feedback
    private Integer feedbackStars;
    private String feedbackComment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    // Required by JPA
    public Order() {
    }

    // --- GETTERS & SETTERS (Standard) ---
    public Long getId() {
        return id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public User getDeliveryStaff() {
        return deliveryStaff;
    }

    public void setDeliveryStaff(User deliveryStaff) {
        this.deliveryStaff = deliveryStaff;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(Double shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Integer getFeedbackStars() {
        return feedbackStars;
    }

    public void setFeedbackStars(Integer feedbackStars) {
        this.feedbackStars = feedbackStars;
    }

    public String getFeedbackComment() {
        return feedbackComment;
    }

    public void setFeedbackComment(String feedbackComment) {
        this.feedbackComment = feedbackComment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}