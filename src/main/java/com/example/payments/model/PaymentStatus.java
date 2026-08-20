package com.example.payments.model;

/**
 * Lifecycle states of a {@link Payment}.
 *
 * <pre>
 *   PENDING  → COMPLETED   (funds moved successfully)
 *   PENDING  → FAILED      (e.g. insufficient funds)
 *   PENDING  → REJECTED    (blocked by fraud screening / validation)
 *   COMPLETED → REFUNDED   (reversed after the fact)
 * </pre>
 */
public enum PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED,
    REJECTED,
    REFUNDED
}

