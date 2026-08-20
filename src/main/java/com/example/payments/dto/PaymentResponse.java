package com.example.payments.dto;

import com.example.payments.model.Payment;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * API view of a {@link Payment} returned to clients.
 */
public record PaymentResponse(
        String id,
        String status,
        String sourceAccountId,
        String destinationAccountId,
        BigDecimal amount,
        String currency,
        String reference,
        String failureReason,
        Instant createdAt
) {

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getStatus().name(),
                payment.getSourceAccountId(),
                payment.getDestinationAccountId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getReference(),
                payment.getFailureReason(),
                payment.getCreatedAt()
        );
    }
}

