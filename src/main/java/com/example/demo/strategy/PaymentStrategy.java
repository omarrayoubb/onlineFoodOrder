package com.example.demo.strategy;

import com.example.demo.dto.PaymentInfo;
import com.example.demo.entity.Order;

/**
 * Strategy interface for payment processing.
 * Different payment methods implement this interface to provide their specific
 * processing logic.
 */
public interface PaymentStrategy {
    /**
     * Process payment for an order.
     * 
     * @param order       The order to process payment for
     * @param paymentInfo Payment information containing method and details
     * @return true if payment processing is successful, false otherwise
     */
    boolean processPayment(Order order, PaymentInfo paymentInfo);
}
