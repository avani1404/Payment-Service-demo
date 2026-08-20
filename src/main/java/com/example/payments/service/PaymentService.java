package com.example.payments.service;

import com.example.payments.dto.CreatePaymentRequest;
import com.example.payments.exception.AccountNotFoundException;
import com.example.payments.exception.CurrencyMismatchException;
import com.example.payments.exception.InsufficientFundsException;
import com.example.payments.exception.PaymentNotFoundException;
import com.example.payments.model.Account;
import com.example.payments.model.Payment;
import com.example.payments.model.PaymentStatus;
import com.example.payments.repository.AccountRepository;
import com.example.payments.repository.PaymentRepository;
import com.example.payments.util.MoneyUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Application service that orchestrates the payment lifecycle:
 * validate → fraud screen → move funds → persist.
 *
 * <p>The money movement (debit + credit) is treated as a unit: if the debit
 * fails (insufficient funds) the payment is marked {@link PaymentStatus#FAILED}
 * and no credit is applied.
 */
@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;
    private final FraudCheckService fraudCheckService;

    public PaymentService(AccountRepository accountRepository,
                          PaymentRepository paymentRepository,
                          FraudCheckService fraudCheckService) {
        this.accountRepository = accountRepository;
        this.paymentRepository = paymentRepository;
        this.fraudCheckService = fraudCheckService;
    }

    public Payment createPayment(CreatePaymentRequest request) {
        BigDecimal amount = MoneyUtils.requirePositive(request.amount(), "amount");
        String currency = request.currency();

        Account source = accountRepository.findById(request.sourceAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.sourceAccountId()));
        Account destination = accountRepository.findById(request.destinationAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.destinationAccountId()));

        requireSameCurrency(source, destination, currency);

        Payment payment = new Payment(
                source.getId(), destination.getId(), amount, currency, request.reference());

        FraudDecision decision = fraudCheckService.assess(payment);
        if (!decision.allowed()) {
            payment.setStatus(PaymentStatus.REJECTED);
            payment.setFailureReason(decision.reason());
            log.info("Payment {} rejected: {}", payment.getId(), decision.reason());
            return paymentRepository.save(payment);
        }

        try {
            source.debit(amount);
            destination.credit(amount);
            payment.setStatus(PaymentStatus.COMPLETED);
            accountRepository.save(source);
            accountRepository.save(destination);
            log.info("Payment {} completed: {} {} {} -> {}",
                    payment.getId(), amount, currency, source.getId(), destination.getId());
        } catch (InsufficientFundsException ex) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason(ex.getMessage());
            log.info("Payment {} failed: {}", payment.getId(), ex.getMessage());
        }

        return paymentRepository.save(payment);
    }

    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    private void requireSameCurrency(Account source, Account destination, String currency) {
        if (!source.getCurrency().equals(currency) || !destination.getCurrency().equals(currency)) {
            throw new CurrencyMismatchException(
                    "Currency %s does not match accounts (%s: %s, %s: %s); FX is not supported"
                            .formatted(currency, source.getId(), source.getCurrency(),
                                    destination.getId(), destination.getCurrency()));
        }
    }
}

