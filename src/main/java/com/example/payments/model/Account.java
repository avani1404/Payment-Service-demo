package com.example.payments.model;

import com.example.payments.exception.InsufficientFundsException;
import com.example.payments.util.MoneyUtils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A ledger account holding a balance in a single ISO-4217 currency.
 *
 * <p>Balance mutations go through {@link #debit(BigDecimal)} /
 * {@link #credit(BigDecimal)}, which keep the amount normalised to 2 decimal
 * places and guard against overdrawing. Methods are {@code synchronized} so a
 * single account can be updated safely from concurrent request threads.
 */
public class Account {

    private final String id;
    private final String ownerName;
    private final String currency;
    private BigDecimal balance;
    private boolean blocked;

    public Account(String id, String ownerName, String currency, BigDecimal balance) {
        this.id = Objects.requireNonNull(id, "id");
        this.ownerName = Objects.requireNonNull(ownerName, "ownerName");
        this.currency = Objects.requireNonNull(currency, "currency");
        this.balance = MoneyUtils.normalize(balance);
        this.blocked = false;
    }

    /** Withdraw {@code amount}; throws if the balance would go negative. */
    public synchronized void debit(BigDecimal amount) {
        BigDecimal value = MoneyUtils.requirePositive(amount, "amount");
        if (balance.compareTo(value) < 0) {
            throw new InsufficientFundsException(id, balance, value);
        }
        balance = MoneyUtils.normalize(balance.subtract(value));
    }

    /** Deposit {@code amount} into the account. */
    public synchronized void credit(BigDecimal amount) {
        BigDecimal value = MoneyUtils.requirePositive(amount, "amount");
        balance = MoneyUtils.normalize(balance.add(value));
    }

    public String getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getCurrency() {
        return currency;
    }

    public synchronized BigDecimal getBalance() {
        return balance;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public String toString() {
        return "Account{id=%s, owner=%s, currency=%s, balance=%s, blocked=%s}"
                .formatted(id, ownerName, currency, balance, blocked);
    }
}

