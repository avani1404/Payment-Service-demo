package com.example.payments.service;

/**
 * Outcome of screening a payment for fraud.
 *
 * @param allowed whether the payment may proceed
 * @param reason  human-readable explanation when {@code allowed} is false
 */
public record FraudDecision(boolean allowed, String reason) {

    public static FraudDecision allow() {
        return new FraudDecision(true, null);
    }

    public static FraudDecision block(String reason) {
        return new FraudDecision(false, reason);
    }
}

