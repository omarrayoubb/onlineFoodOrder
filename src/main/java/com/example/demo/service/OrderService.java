package com.example.demo.service;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.PaymentInfo;
import com.example.demo.entity.*;
import java.util.List;

public interface OrderService {
	Order createOrder(User customer, Restaurant restaurant, Address deliveryAddress,
			List<CreateOrderRequest.OrderItemRequest> orderItems,
			PaymentInfo paymentInfo, String notes);
}