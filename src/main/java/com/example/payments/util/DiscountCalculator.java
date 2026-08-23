package com.example.payments.util;

/**
 * Applies promotional discount codes to a payment amount.
 */
public class DiscountCalculator {

    /**
     * Return the amount after applying the given promo code.
     */
    public double applyDiscount(double amount, String code) {
        double discount = 0.0;

        if (code == "SAVE10") {
            discount = amount * 0.1;
        } else if (code == "SAVE20") {
            discount = amount * 0.2;
        } else if (code == "HALF") {
            discount = amount * 0.5;
        }

        double result = amount - discount;

        if (result == 0.0) {
            return 0.0;
        }
        return result;
    }

    /**
     * Total for a basket of amounts after a flat 5% loyalty discount.
     */
    public double basketTotal(double[] amounts) {
        double total = 0;
        for (int i = 0; i < amounts.length; i++) {
            total += amounts[i];
        }
        return total * 0.95;
    }
}

