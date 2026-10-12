package com.guhecloud.platform.base.util;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ConvertUtilsTests {
    @Test
    public void convert() {
        // Test conversion from String to Integer
        Integer intValue = ConvertUtils.convert(Integer.class, "123");
        assertTrue(intValue != null && intValue == 123);

        // Test conversion from String to Double
        Double doubleValue = ConvertUtils.convert(Double.class, "123.45");
        assertTrue(doubleValue != null && doubleValue.equals(123.45));

        // Test conversion from String to Boolean
        Boolean boolValue = ConvertUtils.convert(Boolean.class, "true");
        assertTrue(boolValue != null && boolValue);

        // Test conversion with null input
        String nullString = ConvertUtils.convert(String.class, null);
        assertNull(nullString);

        // Test conversion with invalid input
        assertThrows(NumberFormatException.class, () -> ConvertUtils.convert(Integer.class, "invalid"));
    }

    @Test
    public void toList() {
        var list = ConvertUtils.toList(String.class, java.util.Arrays.asList("a", "b", "c"));
        assertEquals(3, list.size());
        assertTrue(list.contains("a"));
        assertTrue(list.contains("b"));
        assertTrue(list.contains("c"));
    }

    @Test
    public void toStr() {
        // Test conversion to String
        String strValue = ConvertUtils.toStr(123);
        assertEquals("123", strValue);

        // Test conversion with null input
        String nullStr = ConvertUtils.toStr(null);
        assertNull(nullStr);

        // Test conversion with non-string input
        String boolStr = ConvertUtils.toStr(true);
        assertEquals("true", boolStr);
    }

    @Test
    public void toInt() {
        // Test conversion to Integer
        Integer intValue = ConvertUtils.toInt("123");
        assertEquals(Integer.valueOf(123), intValue);

        // Test conversion with null input
        Integer nullInt = ConvertUtils.toInt(null);
        assertNull(nullInt);

        // Test conversion with invalid input
        assertNull(ConvertUtils.toInt("invalid"));
    }

    @Test
    public void toLong() {
        // Test conversion to Long
        Long longValue = ConvertUtils.toLong("123456789");
        assertEquals(Long.valueOf(123456789), longValue);
    }

    @Test
    public void toDouble() {
        // Test conversion to Double
        Double doubleValue = ConvertUtils.toDouble("123.456");
        assertEquals(Double.valueOf(123.456), doubleValue);

        // Test conversion with null input
        Double nullDouble = ConvertUtils.toDouble(null);
        assertNull(nullDouble);

        // Test conversion with invalid input
        assertNull(ConvertUtils.toDouble("invalid"));
    }

    @Test
    public void toFloat() {
        // Test conversion to Float
        Float floatValue = ConvertUtils.toFloat("123.45");
        assertEquals(Float.valueOf(123.45f), floatValue);

        // Test conversion with null input
        Float nullFloat = ConvertUtils.toFloat(null);
        assertNull(nullFloat);

        // Test conversion with invalid input
        assertNull(ConvertUtils.toFloat("invalid"));
    }

    @Test
    public void toBigDecimal() {
        // Test conversion to BigDecimal
        BigDecimal bigDecimalValue = ConvertUtils.toBigDecimal("123.456");
        assertEquals(new BigDecimal("123.456"), bigDecimalValue);

        // Test conversion with null input
        BigDecimal nullBigDecimal = ConvertUtils.toBigDecimal(null);
        assertNull(nullBigDecimal);

        // Test conversion with invalid input
        assertNull(ConvertUtils.toBigDecimal("invalid"));
    }
}
