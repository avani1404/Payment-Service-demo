package com.example.payments.repository;

import com.example.payments.model.Payment;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * In-memory payment store keyed by payment id.
 */
@Repository
public class PaymentRepository {

    private final Map<String, Payment> payments = new ConcurrentHashMap<>();

    public Payment save(Payment payment) {
        payments.put(payment.getId(), payment);
        return payment;
    }

    public Optional<Payment> findById(String id) {
        return Optional.ofNullable(payments.get(id));
    }

    public List<Payment> findAll() {
        return List.copyOf(payments.values());
    }
}

