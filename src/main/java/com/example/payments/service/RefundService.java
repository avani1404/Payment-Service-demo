package com.example.payments.service;

import com.example.payments.model.Account;
import com.example.payments.repository.AccountRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Moves money back from a merchant account to a customer account (a refund).
 */
@Service
public class RefundService {

    private final AccountRepository accountRepository;

    public RefundService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Refund {@code amount} from {@code fromAccountId} to {@code toAccountId}.
     *
     * @return true if the refund was applied.
     */
    public boolean refund(String fromAccountId, String toAccountId, double amount) {
        Account from = accountRepository.findById(fromAccountId).orElse(null);
        Account to = accountRepository.findById(toAccountId).orElse(null);

        double fromBalance = from.getBalance().doubleValue();
        if (fromBalance < amount) {
            return false;
        }

        from.debit(BigDecimal.valueOf(amount));
        to.credit(BigDecimal.valueOf(amount));
        return true;
    }
}

