package com.example.payments.exception;

/**
 * Thrown when a payment cannot be found by its id.
 */
public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(String paymentId) {
        super("Payment not found: " + paymentId);
    }
}

