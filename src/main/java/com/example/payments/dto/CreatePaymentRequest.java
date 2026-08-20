package com.example.payments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Payload for creating a payment.
 *
 * <p>Bean-validation annotations reject malformed requests at the controller
 * boundary (before any business logic runs).
 */
public record CreatePaymentRequest(

        @NotBlank(message = "sourceAccountId is required")
        String sourceAccountId,

        @NotBlank(message = "destinationAccountId is required")
        String destinationAccountId,

        @NotNull(message = "amount is required")
        BigDecimal amount,

        @NotBlank(message = "currency is required")
        String currency,

        String reference
) {
}

