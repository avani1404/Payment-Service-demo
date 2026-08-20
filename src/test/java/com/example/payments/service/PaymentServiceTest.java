package com.example.payments.service;

import com.example.payments.dto.CreatePaymentRequest;
import com.example.payments.exception.CurrencyMismatchException;
import com.example.payments.exception.PaymentNotFoundException;
import com.example.payments.model.Payment;
import com.example.payments.model.PaymentStatus;
import com.example.payments.repository.AccountRepository;
import com.example.payments.repository.PaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentServiceTest {

    private AccountRepository accountRepository;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        accountRepository = new AccountRepository(); // seeded with sample accounts
        PaymentRepository paymentRepository = new PaymentRepository();
        FraudCheckService fraud = new FraudCheckService(accountRepository, new BigDecimal("10000.00"));
        paymentService = new PaymentService(accountRepository, paymentRepository, fraud);
    }

    @Test
    void completesPaymentAndMovesFunds() {
        CreatePaymentRequest request = new CreatePaymentRequest(
                "ACC-1001", "ACC-1002", new BigDecimal("100.00"), "USD", "test-1");

        Payment payment = paymentService.createPayment(request);

        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
        assertEquals(new BigDecimal("4900.00"), accountRepository.findById("ACC-1001").orElseThrow().getBalance());
        assertEquals(new BigDecimal("1300.00"), accountRepository.findById("ACC-1002").orElseThrow().getBalance());
    }

    @Test
    void failsPaymentOnInsufficientFunds() {
        CreatePaymentRequest request = new CreatePaymentRequest(
                "ACC-1002", "ACC-1001", new BigDecimal("5000.00"), "USD", "too-much");

        Payment payment = paymentService.createPayment(request);

        assertEquals(PaymentStatus.FAILED, payment.getStatus());
        // Balances must be unchanged when a payment fails.
        assertEquals(new BigDecimal("1200.00"), accountRepository.findById("ACC-1002").orElseThrow().getBalance());
        assertEquals(new BigDecimal("5000.00"), accountRepository.findById("ACC-1001").orElseThrow().getBalance());
    }

    @Test
    void rejectsPaymentInvolvingBlockedAccount() {
        CreatePaymentRequest request = new CreatePaymentRequest(
                "ACC-1001", "ACC-9999", new BigDecimal("50.00"), "USD", "to-blocked");

        Payment payment = paymentService.createPayment(request);

        assertEquals(PaymentStatus.REJECTED, payment.getStatus());
    }

    @Test
    void rejectsCurrencyMismatch() {
        CreatePaymentRequest request = new CreatePaymentRequest(
                "ACC-1001", "ACC-1003", new BigDecimal("100.00"), "USD", "fx-not-supported");

        assertThrows(CurrencyMismatchException.class, () -> paymentService.createPayment(request));
    }

    @Test
    void throwsWhenPaymentMissing() {
        assertThrows(PaymentNotFoundException.class, () -> paymentService.getPayment("PAY-does-not-exist"));
    }
}

