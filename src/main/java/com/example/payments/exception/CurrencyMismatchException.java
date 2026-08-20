package com.example.payments.exception;

/**
 * Thrown when the payment currency does not match the accounts' currency.
 * (This service does not perform FX conversion.)
 */
public class CurrencyMismatchException extends RuntimeException {

    public CurrencyMismatchException(String message) {
        super(message);
    }
}

