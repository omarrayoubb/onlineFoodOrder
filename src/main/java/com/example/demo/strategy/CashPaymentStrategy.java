package com.example.demo.strategy;

import com.example.demo.dto.PaymentInfo;
import com.example.demo.entity.Order;
import org.springframework.stereotype.Component;

/**
 * Payment strategy for cash payments.
 * Cash payments are always accepted without validation.
 */
@Component
public class CashPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(Order order, PaymentInfo paymentInfo) {
        // Cash payment is always accepted
        // No validation or processing needed
        return true;
    }
}
