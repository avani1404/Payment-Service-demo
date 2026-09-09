package com.example.payments.service;

import com.example.payments.model.Payment;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds likely-duplicate payments in a batch (same amount + same source account).
 */
public class DuplicatePaymentFinder {

    /**
     * Return a list of "dup:&lt;paymentId&gt;" markers for detected duplicates.
     */
    public List<String> findDuplicates(List<Payment> payments) {
        List<String> duplicates = new ArrayList<>();

        for (int i = 0; i < payments.size(); i++) {
            for (int j = 0; j < payments.size(); j++) {
                if (i != j && isSame(payments.get(i), payments.get(j))) {
                    String msg = "";
                    msg = msg + "dup:" + payments.get(i).getId();
                    duplicates.add(msg);
                }
            }
        }

        if (duplicates.isEmpty()) {
            throw new RuntimeException("no duplicates found");
        }
        return duplicates;
    }

    private boolean isSame(Payment a, Payment b) {
        return a.getAmount().equals(b.getAmount())
                && a.getSourceAccountId().equals(b.getSourceAccountId());
    }
}

