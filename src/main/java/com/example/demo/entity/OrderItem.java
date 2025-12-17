package com.example.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.List;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "food_item_id")
    private FoodItem foodItem;

    // Snapshot: "Burger" (In case FoodItem is deleted later)
    private String itemNameSnapshot;

    // --- DECORATOR RESULT ---
    // Example: ["Extra Cheese", "No Onions"]
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> selectedAdditions;

    private Double calculatedPrice; // Base Price + Additions

    // --- CONSTRUCTORS ---
    public OrderItem() {}

    public OrderItem(FoodItem foodItem, List<String> selectedAdditions, Double calculatedPrice) {
        this.foodItem = foodItem;
        this.itemNameSnapshot = foodItem.getName();
        this.selectedAdditions = selectedAdditions;
        this.calculatedPrice = calculatedPrice;
    }

    // --- GETTERS & SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public FoodItem getFoodItem() { return foodItem; }
    public void setFoodItem(FoodItem foodItem) { this.foodItem = foodItem; }

    public String getItemNameSnapshot() { return itemNameSnapshot; }
    public void setItemNameSnapshot(String itemNameSnapshot) { this.itemNameSnapshot = itemNameSnapshot; }

    public List<String> getSelectedAdditions() { return selectedAdditions; }
    public void setSelectedAdditions(List<String> selectedAdditions) { this.selectedAdditions = selectedAdditions; }

    public Double getCalculatedPrice() { return calculatedPrice; }
    public void setCalculatedPrice(Double calculatedPrice) { this.calculatedPrice = calculatedPrice; }
}