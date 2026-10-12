package com.guhecloud.platform.base.util;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BizCodeUtilsTests {
    @Test
    public void generate() {
        assertTrue(BizCodeUtils.generate().matches("^\\d{14}_\\d{4}$"));
        assertTrue(BizCodeUtils.generate("test").matches("^test\\d{14}_\\d{4}$"));
        assertTrue(BizCodeUtils.generateNoRandom("test").matches("^test\\d{14}$"));
    }

    @Test
    public void dailySequenceCode() {
        String prefix = BizCodeUtils.dailyPrefix("KP-", LocalDate.of(2026, 5, 21), "-");
        assertEquals("KP-20260521-", prefix);
        assertEquals("KP-20260521-001", BizCodeUtils.nextSequenceCode(prefix, null, 3));
        assertEquals("KP-20260521-010", BizCodeUtils.nextSequenceCode(prefix, "KP-20260521-009", 3));
    }

    @Test(expected = IllegalStateException.class)
    public void nextSequenceCodeRejectsOverflow() {
        BizCodeUtils.nextSequenceCode("KP-20260521-", "KP-20260521-999", 3);
    }
}
