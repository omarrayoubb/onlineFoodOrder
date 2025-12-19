package com.example.demo.strategy;

import com.example.demo.dto.PaymentInfo;
import com.example.demo.entity.Order;
import org.springframework.stereotype.Component;

/**
 * Payment strategy for credit card payments.
 * Validates that credit card information is present.
 * Note: Credit card information is NOT stored in the database for security.
 */
@Component
public class CreditCardPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(Order order, PaymentInfo paymentInfo) {
        // Validate that credit card information is present
        if (paymentInfo == null) {
            throw new IllegalArgumentException("Payment information is required for credit card payment");
        }

        if (paymentInfo.getCardNumber() == null || paymentInfo.getCardNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Card number is required for credit card payment");
        }

        if (paymentInfo.getCardholderName() == null || paymentInfo.getCardholderName().trim().isEmpty()) {
            throw new IllegalArgumentException("Cardholder name is required for credit card payment");
        }

        if (paymentInfo.getExpiry() == null || paymentInfo.getExpiry().trim().isEmpty()) {
            throw new IllegalArgumentException("Expiry date is required for credit card payment");
        }

        if (paymentInfo.getCvv() == null || paymentInfo.getCvv().trim().isEmpty()) {
            throw new IllegalArgumentException("CVV is required for credit card payment");
        }

        // Payment info is validated and present
        // Note: We do NOT store credit card information in the database
        // Only the payment method (CREDIT_CARD) is stored in the Order entity
        return true;
    }
}
