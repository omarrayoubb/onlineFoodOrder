package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO for creating an order.
 */
public class CreateOrderRequest {

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;

    @NotNull(message = "Address ID is required")
    private Long addressId;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequest> items;

    @NotNull(message = "Payment information is required")
    @Valid
    private PaymentInfo paymentInfo;

    private String notes; // Optional notes field

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(Long restaurantId, Long addressId, List<OrderItemRequest> items) {
        this.restaurantId = restaurantId;
        this.addressId = addressId;
        this.items = items;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * DTO for order item requests.
     */
    public static class OrderItemRequest {
        @NotNull(message = "Food item ID is required")
        private Long foodItemId;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        private List<String> selectedAdditions;

        public OrderItemRequest() {
        }

        public OrderItemRequest(Long foodItemId, List<String> selectedAdditions) {
            this.foodItemId = foodItemId;
            this.quantity = 1;
            this.selectedAdditions = selectedAdditions;
        }

        public OrderItemRequest(Long foodItemId, Integer quantity, List<String> selectedAdditions) {
            this.foodItemId = foodItemId;
            this.quantity = quantity;
            this.selectedAdditions = selectedAdditions;
        }

        public Long getFoodItemId() {
            return foodItemId;
        }

        public void setFoodItemId(Long foodItemId) {
            this.foodItemId = foodItemId;
        }

        public List<String> getSelectedAdditions() {
            return selectedAdditions;
        }

        public void setSelectedAdditions(List<String> selectedAdditions) {
            this.selectedAdditions = selectedAdditions;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
