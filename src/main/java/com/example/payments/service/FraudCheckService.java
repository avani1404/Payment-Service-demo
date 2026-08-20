package com.example.payments.service;

import com.example.payments.model.Account;
import com.example.payments.model.Payment;
import com.example.payments.repository.AccountRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Very small, rule-based fraud screen.
 *
 * <p>Rules (in order):
 * <ol>
 *   <li>Reject payments to/from an account flagged as blocked.</li>
 *   <li>Reject payments above a configurable single-transaction threshold
 *       ({@code payments.fraud.max-amount}).</li>
 * </ol>
 * A production system would layer velocity checks, device fingerprinting,
 * ML scoring, etc. — this is intentionally simple and deterministic.
 */
@Service
public class FraudCheckService {

    private static final Logger log = LoggerFactory.getLogger(FraudCheckService.class);

    private final AccountRepository accountRepository;
    private final BigDecimal maxAmount;

    public FraudCheckService(AccountRepository accountRepository,
                             @Value("${payments.fraud.max-amount:10000.00}") BigDecimal maxAmount) {
        this.accountRepository = accountRepository;
        this.maxAmount = maxAmount;
    }

    public FraudDecision assess(Payment payment) {
        if (isBlocked(payment.getSourceAccountId()) || isBlocked(payment.getDestinationAccountId())) {
            log.warn("Blocking payment {} — involves a blocked account", payment.getId());
            return FraudDecision.block("One of the accounts is blocked");
        }

        if (payment.getAmount().compareTo(maxAmount) > 0) {
            log.warn("Blocking payment {} — amount {} exceeds threshold {}",
                    payment.getId(), payment.getAmount(), maxAmount);
            return FraudDecision.block("Amount exceeds the single-transaction limit of " + maxAmount);
        }

        return FraudDecision.allow();
    }

    private boolean isBlocked(String accountId) {
        return accountRepository.findById(accountId).map(Account::isBlocked).orElse(false);
    }
}

