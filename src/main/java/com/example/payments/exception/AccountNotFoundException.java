package com.example.payments.exception;

/**
 * Thrown when a referenced account does not exist.
 */
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String accountId) {
        super("Account not found: " + accountId);
    }
}

