package com.example.payments.metrics;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks how many payments have been processed, per currency.
 *
 * <p>A single instance is shared across all request threads.
 */
@Component
public class PaymentMetrics {

    private final Map<String, Integer> counters = new HashMap<>();
    private long totalProcessed = 0;

    /**
     * Record one processed payment in the given currency.
     */
    public void record(String currency) {
        Integer current = counters.get(currency);
        if (current == null) {
            counters.put(currency, 1);
        } else {
            counters.put(currency, current + 1);
        }
        totalProcessed++;
    }

    public Map<String, Integer> snapshot() {
        return counters;
    }

    public long getTotalProcessed() {
        return totalProcessed;
    }
}

