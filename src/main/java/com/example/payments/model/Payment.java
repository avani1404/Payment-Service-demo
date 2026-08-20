package com.example.payments.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * A single money movement between two accounts.
 *
 * <p>Created in {@link PaymentStatus#PENDING} and transitioned by the service
 * layer as it is screened and settled. {@code failureReason} is populated when
 * a payment ends in {@link PaymentStatus#FAILED} or {@link PaymentStatus#REJECTED}.
 */
public class Payment {

    private final String id;
    private final String sourceAccountId;
    private final String destinationAccountId;
    private final BigDecimal amount;
    private final String currency;
    private final String reference;
    private final Instant createdAt;

    private PaymentStatus status;
    private String failureReason;

    public Payment(String sourceAccountId,
                   String destinationAccountId,
                   BigDecimal amount,
                   String currency,
                   String reference) {
        this.id = "PAY-" + UUID.randomUUID();
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.currency = currency;
        this.reference = reference;
        this.createdAt = Instant.now();
        this.status = PaymentStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public String getDestinationAccountId() {
        return destinationAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getReference() {
        return reference;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}

