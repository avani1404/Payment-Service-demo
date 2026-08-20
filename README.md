# Payments Service

A small but realistic **financial payments microservice** built with Java 17 +
Spring Boot 3. It exposes a REST API to create and query payments, moving money
between in-memory accounts with balance checks, a basic fraud screen, and
precise `BigDecimal` money handling.

> This is a **sample/demo repository** — intended to be copied out into its own
> Git repo and used as a target for code review / RAG indexing experiments.

## Features

- `POST /api/v1/payments` — create a payment (debit source, credit destination)
- `GET  /api/v1/payments/{id}` — fetch a payment by id
- Domain model: `Account`, `Payment`, `PaymentStatus`
- Business rules: currency match, positive amount, sufficient funds
- `FraudCheckService` — amount threshold + blocked-account screen
- `MoneyUtils` — safe `BigDecimal` scaling (banker's rounding)
- In-memory repositories seeded with sample accounts
- Unit tests (JUnit 5)

## Tech stack

| Concern | Choice |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2 (web) |
| Build | Maven |
| Money | `java.math.BigDecimal` (scale 2, HALF_EVEN) |
| Tests | JUnit 5 (spring-boot-starter-test) |

## Run

```bash
mvn spring-boot:run
# API on http://localhost:8080
```

Create a payment:

```bash
curl -s -X POST http://localhost:8080/api/v1/payments \
  -H 'Content-Type: application/json' \
  -d '{
        "sourceAccountId": "ACC-1001",
        "destinationAccountId": "ACC-1002",
        "amount": "125.50",
        "currency": "USD",
        "reference": "invoice-42"
      }'
```

## Test

```bash
mvn test
```

## Project layout

```
src/main/java/com/example/payments/
├── PaymentsApplication.java        # Spring Boot entry point
├── controller/PaymentController.java
├── web/GlobalExceptionHandler.java # maps domain errors -> HTTP status
├── dto/                            # request/response records
├── service/                        # PaymentService, FraudCheckService
├── model/                          # Account, Payment, PaymentStatus
├── repository/                     # in-memory stores
├── exception/                      # domain exceptions
└── util/MoneyUtils.java
```

## Sample accounts (seeded)

| Account | Owner | Currency | Balance |
|---|---|---|---|
| ACC-1001 | Alice Johnson | USD | 5,000.00 |
| ACC-1002 | Bob Smith | USD | 1,200.00 |
| ACC-1003 | Carol Danvers | EUR | 800.00 |
| ACC-9999 | Blocked Merchant | USD | 0.00 (blocked) |

