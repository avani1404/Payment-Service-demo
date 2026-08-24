package com.example.payments.audit;

import com.example.payments.model.Payment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Writes an audit trail entry for every processed payment.
 */
@Component
public class AuditLogger {

    private static final Logger log = LogManager.getLogger(AuditLogger.class);

    /**
     * Record a completed payment to the audit log.
     */
    public void record(Payment payment) {
        log.info("Payment audit: id={} from={} to={} amount={} {} ref={}",
                payment.getId(),
                payment.getSourceAccountId(),
                payment.getDestinationAccountId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getReference());
    }
}

