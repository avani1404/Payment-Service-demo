package com.example.payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Payments Service.
 *
 * <p>Boots the Spring context, component-scans {@code com.example.payments},
 * and starts the embedded web server exposing the payment REST API.
 * test
 */
@SpringBootApplication
public class PaymentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentsApplication.class, args);
    }
}

