package com.example.payments.repository;

import com.example.payments.model.Account;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * In-memory account store, seeded with a few sample accounts on startup.
 *
 * <p>Backed by a {@link ConcurrentHashMap} so it is safe under concurrent
 * requests. A real service would persist to a database via JPA/JDBC.
 */
@Repository
public class AccountRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    public AccountRepository() {
        seed();
    }

    private void seed() {
        save(new Account("ACC-1001", "Alice Johnson", "USD", new BigDecimal("5000.00")));
        save(new Account("ACC-1002", "Bob Smith", "USD", new BigDecimal("1200.00")));
        save(new Account("ACC-1003", "Carol Danvers", "EUR", new BigDecimal("800.00")));

        Account blocked = new Account("ACC-9999", "Blocked Merchant", "USD", new BigDecimal("0.00"));
        blocked.setBlocked(true);
        save(blocked);
    }

    public Optional<Account> findById(String id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public Account save(Account account) {
        accounts.put(account.getId(), account);
        return account;
    }

    public boolean exists(String id) {
        return accounts.containsKey(id);
    }
}

