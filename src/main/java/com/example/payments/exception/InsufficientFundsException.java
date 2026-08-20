package com.example.payments.exception;

import java.math.BigDecimal;

/**
 * Thrown when an account does not have enough balance to cover a debit.
 */
public class InsufficientFundsException extends RuntimeException {

    private final String accountId;

    public InsufficientFundsException(String accountId, BigDecimal balance, BigDecimal requested) {
        super("Account %s has insufficient funds: balance=%s, requested=%s"
                .formatted(accountId, balance, requested));
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}

