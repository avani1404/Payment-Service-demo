package com.example.payments.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoneyUtilsTest {

    @Test
    void normalizeScalesToTwoDecimalsWithBankersRounding() {
        assertEquals(new BigDecimal("1.00"), MoneyUtils.normalize(new BigDecimal("1")));
        // HALF_EVEN: 2.125 -> 2.12 (rounds to the even neighbour)
        assertEquals(new BigDecimal("2.12"), MoneyUtils.normalize(new BigDecimal("2.125")));
        assertEquals(new BigDecimal("2.14"), MoneyUtils.normalize(new BigDecimal("2.135")));
    }

    @Test
    void parseReadsStringAmounts() {
        assertEquals(new BigDecimal("125.50"), MoneyUtils.parse("125.5"));
        assertEquals(new BigDecimal("0.00"), MoneyUtils.parse(" 0 "));
    }

    @Test
    void parseRejectsBlank() {
        assertThrows(IllegalArgumentException.class, () -> MoneyUtils.parse("  "));
    }

    @Test
    void isPositiveChecksStrictlyGreaterThanZero() {
        assertTrue(MoneyUtils.isPositive(new BigDecimal("0.01")));
        assertFalse(MoneyUtils.isPositive(BigDecimal.ZERO));
        assertFalse(MoneyUtils.isPositive(new BigDecimal("-1.00")));
    }

    @Test
    void requirePositiveRejectsNonPositive() {
        assertThrows(IllegalArgumentException.class,
                () -> MoneyUtils.requirePositive(BigDecimal.ZERO, "amount"));
        assertEquals(new BigDecimal("10.00"),
                MoneyUtils.requirePositive(new BigDecimal("10"), "amount"));
    }
}

