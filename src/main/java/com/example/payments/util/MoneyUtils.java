package com.example.payments.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Helpers for handling monetary amounts with {@link BigDecimal}.
 *
 * <p>Money must never be represented as {@code double}/{@code float} — binary
 * floating point cannot exactly represent decimal fractions like {@code 0.10},
 * which leads to rounding drift. All amounts here are normalised to a scale of
 * 2 using {@link RoundingMode#HALF_EVEN} ("banker's rounding"), the standard for
 * financial calculations.
 */
public final class MoneyUtils {

    /** Number of decimal places for a minor currency unit (cents). */
    public static final int SCALE = 2;

    private MoneyUtils() {
        // utility class — no instances
    }

    /** Normalise to 2 decimal places using banker's rounding. */
    public static BigDecimal normalize(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("amount must not be null");
        }
        return amount.setScale(SCALE, RoundingMode.HALF_EVEN);
    }

    /** Parse a string amount (e.g. "125.50") into a normalised BigDecimal. */
    public static BigDecimal parse(String amount) {
        if (amount == null || amount.isBlank()) {
            throw new IllegalArgumentException("amount must not be blank");
        }
        return normalize(new BigDecimal(amount.trim()));
    }

    /** True when the amount is strictly greater than zero. */
    public static boolean isPositive(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Validate that {@code amount} is present and strictly positive, returning
     * the normalised value.
     *
     * @throws IllegalArgumentException if null or not &gt; 0
     */
    public static BigDecimal requirePositive(BigDecimal amount, String fieldName) {
        if (!isPositive(amount)) {
            throw new IllegalArgumentException(fieldName + " must be greater than zero");
        }
        return normalize(amount);
    }
}

