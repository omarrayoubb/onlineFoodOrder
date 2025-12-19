package com.example.demo.factory;

import com.example.demo.enums.PaymentMethod;
import com.example.demo.strategy.CashPaymentStrategy;
import com.example.demo.strategy.CreditCardPaymentStrategy;
import com.example.demo.strategy.PaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory class to return the appropriate payment strategy based on payment
 * method.
 */
@Component
public class PaymentStrategyFactory {

    private final CashPaymentStrategy cashPaymentStrategy;
    private final CreditCardPaymentStrategy creditCardPaymentStrategy;

    @Autowired
    public PaymentStrategyFactory(CashPaymentStrategy cashPaymentStrategy,
            CreditCardPaymentStrategy creditCardPaymentStrategy) {
        this.cashPaymentStrategy = cashPaymentStrategy;
        this.creditCardPaymentStrategy = creditCardPaymentStrategy;
    }

    /**
     * Returns the appropriate payment strategy based on payment method.
     * 
     * @param method The payment method (CASH or CREDIT_CARD)
     * @return The corresponding PaymentStrategy implementation
     * @throws IllegalArgumentException if payment method is invalid
     */
    public PaymentStrategy getStrategy(PaymentMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }

        return switch (method) {
            case CASH -> cashPaymentStrategy;
            case CREDIT_CARD -> creditCardPaymentStrategy;
        };
    }
}
