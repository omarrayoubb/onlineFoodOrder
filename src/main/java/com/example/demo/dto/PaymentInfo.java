package com.example.demo.dto;

import com.example.demo.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for payment information.
 * Credit card fields are required when paymentMethod is CREDIT_CARD.
 * For CASH payment, all card fields are optional/null.
 */
public class PaymentInfo {

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    // Credit card fields - required when paymentMethod is CREDIT_CARD
    private String cardNumber; // Format: "1234 5678 9012 3456"

    private String cardholderName; // e.g., "John Doe"

    private String expiry; // Format: "MM/YY"

    private String cvv; // 3-4 digit CVV

    public PaymentInfo() {
    }

    public PaymentInfo(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentInfo(PaymentMethod paymentMethod, String cardNumber, String cardholderName, String expiry,
            String cvv) {
        this.paymentMethod = paymentMethod;
        this.cardNumber = cardNumber;
        this.cardholderName = cardholderName;
        this.expiry = expiry;
        this.cvv = cvv;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
