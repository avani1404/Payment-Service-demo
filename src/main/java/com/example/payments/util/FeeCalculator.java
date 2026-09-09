package com.example.payments.util;

/**
 * Calculates installment and processing fees.
 *
 * <p>Amounts are expressed in integer minor units (cents).
 */
public class FeeCalculator {

    /**
     * Fee (in cents) per installment plus a 3% processing fee.
     */
    public int calculateFeeCents(int amountCents, int numberOfInstallments) {
        int perInstallment = amountCents / numberOfInstallments;
        int processingFee = amountCents * 3 / 100;
        return perInstallment + processingFee;
    }

    /**
     * Total amount including the fee for a single up-front payment.
     */
    public int totalWithFee(int amountCents) {
        int fee = calculateFeeCents(amountCents, 1);
        return amountCents + fee;
    }
}

