package com.guhecloud.platform.base.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class StringUtilsTests {
    @Test
    public void isEmpty() {
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("1"));
        assertFalse(StringUtils.isEmpty(" 1 "));
    }

    @Test
    public void isNotEmpty() {
        assertFalse(StringUtils.isNotEmpty(null));
        assertFalse(StringUtils.isNotEmpty(""));
        assertTrue(StringUtils.isNotEmpty(" "));
        assertTrue(StringUtils.isNotEmpty("1"));
        assertTrue(StringUtils.isNotEmpty(" 1 "));
    }

    @Test
    public void isBlank() {
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertFalse(StringUtils.isBlank("1"));
        assertFalse(StringUtils.isBlank(" 1 "));
    }

    @Test
    public void isNotBlank() {
        assertFalse(StringUtils.isNotBlank(null));
        assertFalse(StringUtils.isNotBlank(""));
        assertFalse(StringUtils.isNotBlank(" "));
        assertTrue(StringUtils.isNotBlank("1"));
        assertTrue(StringUtils.isNotBlank(" 1 "));
    }

    @Test
    public void containsEmpty() {
        assertFalse(StringUtils.containsEmpty());
        assertTrue(StringUtils.containsEmpty((String) null));
        assertTrue(StringUtils.containsEmpty(""));
        assertFalse(StringUtils.containsEmpty(" ", "1"));
        assertFalse(StringUtils.containsEmpty("1", "2"));
        assertFalse(StringUtils.containsEmpty("1", "2", "3"));
        assertTrue(StringUtils.containsEmpty("1", "", "3"));
        assertTrue(StringUtils.containsEmpty("1", null, "3"));
    }

    @Test
    public void containsBlank() {
        assertFalse(StringUtils.containsBlank());
        assertTrue(StringUtils.containsBlank((String) null));
        assertTrue(StringUtils.containsBlank(""));
        assertTrue(StringUtils.containsBlank(" ", "1"));
        assertFalse(StringUtils.containsBlank("1", "2"));
        assertFalse(StringUtils.containsBlank("1", "2", "3"));
        assertTrue(StringUtils.containsBlank("1", "", "3"));
        assertTrue(StringUtils.containsBlank("1", null, "3"));
    }

    @Test
    public void substring() {
        assertThrows(IllegalArgumentException.class, () -> StringUtils.substring("", -1, -1));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.substring(null, -1));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.substring(null, -1, 0));
        assertEquals("", StringUtils.substring("123", 0, 0));
        assertEquals("1", StringUtils.substring("123", 0, 1));
        assertEquals("3", StringUtils.substring("123", -1, 1));
        assertEquals("56", StringUtils.substring("123456", -2, 2));
        assertEquals("456", StringUtils.substring("123456", -3));
        assertEquals("23456", StringUtils.substring("123456", 1));
        assertEquals("123456", StringUtils.substring("123456", 0));
        assertEquals("6", StringUtils.substring("123456", -1));
    }

    @Test
    public void just() {
        assertThrows(IllegalArgumentException.class, () -> StringUtils.just("", -1, ' ', true));
        assertEquals("##aaa", StringUtils.just("aaa", 5, '#', false));
        assertEquals("aaa##", StringUtils.just("aaa", 5, '#', true));
        assertEquals("#####", StringUtils.just("", 5, '#', true));
    }

    @Test
    public void justLeft() {
        assertEquals("##aaa", StringUtils.justLeft("aaa", 5, '#'));
    }

    @Test
    public void justRight() {
        assertEquals("aaa##", StringUtils.justRight("aaa", 5, '#'));
        assertEquals("aaa  ", StringUtils.justRight("aaa", 5));
    }

    @Test
    public void join() {
        assertEquals("", StringUtils.join(","));
        assertEquals(",", StringUtils.join(",", null, null));
        assertEquals("", StringUtils.join(","));
        assertEquals(",", StringUtils.join(",", null, null));
        assertEquals("a-b", StringUtils.join("-", "a", "b"));
        assertEquals("-b", StringUtils.join("-", "", "b"));
        assertEquals("-b", StringUtils.join("-", null, "b"));
        assertEquals("ab", StringUtils.join(null, "a", "b"));
        assertEquals("ab", StringUtils.join("", "a", "b"));

        assertEquals("a-b", StringUtils.join("-", List.of("a", "b")));
        assertEquals("-b", StringUtils.join("-", List.of("", "b")));
        assertEquals("ab", StringUtils.join(null, List.of("a", "b")));
        assertEquals("ab", StringUtils.join("", List.of("a", "b")));
    }

    @Test
    public void split() {
        assertThrows(PatternSyntaxException.class, () -> StringUtils.split("1", "*"));
        assertTrue(StringUtils.split(null, ",").isEmpty());
        assertTrue(StringUtils.split("", ",").isEmpty());
        assertEquals("1", StringUtils.split("1,2", ",").getFirst());
        assertEquals("", StringUtils.split(",1,2", ",").getFirst());
        assertEquals(" 2", StringUtils.split("1, 2", ",").getLast());
        assertEquals(" 2", StringUtils.split("1 , 2", ",").getLast());
        assertEquals(" 2", StringUtils.split("1 ,,,, 2", ",+").getLast());
        assertEquals("1", StringUtils.split("1 ,,,, 2", ",*").getFirst());
        assertEquals("2", StringUtils.split("1 ,,,, 2", ",*").getLast());
        assertEquals("", StringUtils.split("1 ,,,, 2", ",*").get(2));
        assertTrue(StringUtils.split("1 ,,,, 2", ".*").isEmpty());
    }

    @Test
    public void splitAndIgnoreBlank() {
        assertEquals("2", StringUtils.splitAndIgnoreBlank("1,,2", ",").get(1));
        assertEquals("2", StringUtils.splitAndIgnoreBlank("1, ,2", ",").get(1));
    }

    @Test
    public void splitAndTranslate() {
        assertEquals(Long.valueOf(1), StringUtils.splitAndTranslate("1,2", Long::valueOf, ",").getFirst());
        assertEquals(Long.valueOf(2), StringUtils.splitAndTranslate("1,2", Long::valueOf, ",").getLast());
    }

    @Test
    public void placeholderReplace() {
        var map = new HashMap<String, String>();
        map.put("abc", "123");
        assertEquals("123", StringUtils.placeholderReplace("${abc}", map));
        assertEquals("#123#", StringUtils.placeholderReplace("#${abc}#", map));
    }

    @Test
    public void equalsAny() {
        assertFalse(StringUtils.equalsAny(null, (CharSequence[]) null));
        assertFalse(StringUtils.equalsAny(null, null, null));
        assertFalse(StringUtils.equalsAny(null, "abc", "def"));
        assertFalse(StringUtils.equalsAny("abc", null, "def"));
        assertTrue(StringUtils.equalsAny("abc", "abc", "def"));
        assertFalse(StringUtils.equalsAny("abc", "ABC", "DEF"));
    }

    @Test
    public void equals() {
        assertTrue(StringUtils.equals("", ""));
        assertTrue(StringUtils.equals(null, null));
        assertFalse(StringUtils.equals("1", "2"));
    }

    @Test
    public void contains() {
        assertTrue(StringUtils.contains("abcdef", "def"));
    }

    @Test
    public void endsWith() {
        assertTrue(StringUtils.endsWith("abcdef", "def"));
        assertFalse(StringUtils.endsWith("abcdefg", "def"));
    }

    @Test
    public void substringBefore() {
        assertNull(StringUtils.substringBefore(null, "def"));
        assertEquals("", StringUtils.substringBefore("", "def"));
        assertEquals("", StringUtils.substringBefore("abcdef", "a"));
        assertEquals("a", StringUtils.substringBefore("abcba", "b"));
        assertEquals("ab", StringUtils.substringBefore("abc", "c"));
        assertEquals("abc", StringUtils.substringBefore("abc", "d"));
        assertEquals("", StringUtils.substringBefore("abc", ""));
        assertEquals("abc", StringUtils.substringBefore("abc", null));
    }

    @Test
    public void capitalize() {
        assertNull(StringUtils.capitalize(null));
        assertEquals("", StringUtils.capitalize(""));
        assertEquals("Cat", StringUtils.capitalize("cat"));
        assertEquals("CAt", StringUtils.capitalize("CAt"));
        assertEquals("'cat'", StringUtils.capitalize("'cat'"));
    }

    @Test
    public void isAnyBlank() {
        assertTrue(StringUtils.isAnyBlank((String) null));
        assertTrue(StringUtils.isAnyBlank((String[]) null));
        assertTrue(StringUtils.isAnyBlank(null, "foo"));
        assertTrue(StringUtils.isAnyBlank(null, null));
        assertTrue(StringUtils.isAnyBlank("", "bar"));
        assertTrue(StringUtils.isAnyBlank("bob", ""));
        assertTrue(StringUtils.isAnyBlank("  bob  ", null));
        assertTrue(StringUtils.isAnyBlank(" ", "bar"));
        assertTrue(StringUtils.isAnyBlank(new String[]{}));
        assertTrue(StringUtils.isAnyBlank(new String[]{ "" }));
        assertFalse(StringUtils.isAnyBlank("foo", "bar"));
    }

    @Test
    public void isNoneBlank() {
        assertFalse(StringUtils.isNoneBlank((String) null));
        assertFalse(StringUtils.isNoneBlank((String[]) null));
        assertFalse(StringUtils.isNoneBlank(null, "foo"));
        assertFalse(StringUtils.isNoneBlank(null, null));
        assertFalse(StringUtils.isNoneBlank("", "bar"));
        assertFalse(StringUtils.isNoneBlank("bob", ""));
        assertFalse(StringUtils.isNoneBlank("  bob  ", null));
        assertFalse(StringUtils.isNoneBlank(" ", "bar"));
        assertFalse(StringUtils.isNoneBlank(new String[]{}));
        assertFalse(StringUtils.isNoneBlank(new String[]{ "" }));
        assertTrue(StringUtils.isNoneBlank("foo", "bar"));

    }

    @Test
    public void isNumeric() {
        assertFalse(StringUtils.isNumeric(null));
        assertFalse(StringUtils.isNumeric(""));
        assertFalse(StringUtils.isNumeric("  "));
        assertFalse(StringUtils.isNumeric("12 3"));
        assertFalse(StringUtils.isNumeric("ab2c"));
        assertFalse(StringUtils.isNumeric("12-3"));
        assertFalse(StringUtils.isNumeric("12.3"));
        assertFalse(StringUtils.isNumeric("-123"));
        assertFalse(StringUtils.isNumeric("+123"));
        assertTrue(StringUtils.isNumeric("\u0967\u0968\u0969"));
        assertTrue(StringUtils.isNumeric("123"));
    }

    @Test
    public void isNumericSpace() {
        assertFalse(StringUtils.isNumericSpace(null));
        assertTrue(StringUtils.isNumericSpace(""));
        assertTrue(StringUtils.isNumericSpace("  "));
        assertTrue(StringUtils.isNumericSpace("123"));
        assertTrue(StringUtils.isNumericSpace("12 3"));
        assertTrue(StringUtils.isNumericSpace("१२३"));
        assertTrue(StringUtils.isNumericSpace("१२ ३"));
        assertFalse(StringUtils.isNumericSpace("ab2c"));
        assertFalse(StringUtils.isNumericSpace("12-3"));
        assertFalse(StringUtils.isNumericSpace("12.3"));
    }

    @Test
    public void isNotNumericSpace() {
        assertTrue(StringUtils.isNotNumericSpace(null));
        assertFalse(StringUtils.isNotNumericSpace(""));
        assertFalse(StringUtils.isNotNumericSpace("  "));
        assertFalse(StringUtils.isNotNumericSpace("123"));
        assertFalse(StringUtils.isNotNumericSpace("12 3"));
        assertFalse(StringUtils.isNotNumericSpace("१२३"));
        assertFalse(StringUtils.isNotNumericSpace("१२ ३"));
        assertTrue(StringUtils.isNotNumericSpace("ab2c"));
        assertTrue(StringUtils.isNotNumericSpace("12-3"));
        assertTrue(StringUtils.isNotNumericSpace("12.3"));
    }

    @Test
    public void startsWithIgnoreCase() {
        assertTrue(StringUtils.startsWithIgnoreCase(null, null));
        assertFalse(StringUtils.startsWithIgnoreCase(null, "abc"));
        assertFalse(StringUtils.startsWithIgnoreCase("abcdef", null));
        assertTrue(StringUtils.startsWithIgnoreCase("abcdef", "abc"));
        assertTrue(StringUtils.startsWithIgnoreCase("ABCDEF", "abc"));
    }

    @Test
    public void containsAny() {
        assertFalse(StringUtils.containsAny(null, null));
        assertFalse(StringUtils.containsAny("", "123"));
        assertFalse(StringUtils.containsAny("123", null));
        assertFalse(StringUtils.containsAny("123", ""));
        assertTrue(StringUtils.containsAny("abcd", "ab"));
        assertTrue(StringUtils.containsAny("abcd", "bc"));
        assertTrue(StringUtils.containsAny("abcd", "ad"));
        assertTrue(StringUtils.containsAny("abcd", "\tc"));
        assertTrue(StringUtils.containsAny("abcd", "$.#bF"));
        assertFalse(StringUtils.containsAny("abcd", "z"));
    }

    @Test
    public void joinWith() {
        assertEquals("a,b", StringUtils.joinWith(",", "a", "b"));
        assertEquals("a,b,", StringUtils.joinWith(",", "a", "b", ""));
        assertEquals("a,,b", StringUtils.joinWith(",", "a", null, "b"));
        assertEquals("ab", StringUtils.joinWith(null, "a", "b"));
        assertEquals("ab", StringUtils.joinWith("", "a", "b"));
    }

    @Test
    public void equalsIgnoreCase() {
        assertTrue(StringUtils.equalsIgnoreCase(null, null));
        assertFalse(StringUtils.equalsIgnoreCase(null, "abc"));
        assertFalse(StringUtils.equalsIgnoreCase("abc", null));
        assertTrue(StringUtils.equalsIgnoreCase("abc", "abc"));
        assertTrue(StringUtils.equalsIgnoreCase("abc", "ABC"));
    }

    @Test
    public void toCamelCase() {
        assertEquals("ABC", StringUtils.toCamelCase("ABC"));
        assertEquals("abc", StringUtils.toCamelCase("abc"));
        assertEquals("abcDef", StringUtils.toCamelCase("abc_def"));
        assertEquals("abcDef", StringUtils.toCamelCase("abc.def", '.'));
    }

    @Test
    public void repeat() {
        assertEquals("abcdefabcdefabcdef", StringUtils.repeat("abcdef", 3));
        assertEquals("aaa", StringUtils.repeat("a", 3));
    }

    @Test
    public void defaultIfEmpty() {
        assertEquals("abcdef", StringUtils.defaultIfEmpty("abcdef", "a"));
        assertEquals("a", StringUtils.defaultIfEmpty("", "a"));
    }

    @Test
    public void defaultIfBlank() {
        assertEquals("abcdef", StringUtils.defaultIfBlank("abcdef", "a"));
        assertEquals("a", StringUtils.defaultIfBlank("", "a"));
        assertEquals("a", StringUtils.defaultIfBlank(" ", "a"));
    }

    @Test
    public void defaultString() {
        assertEquals("abcdef", StringUtils.defaultString("abcdef"));
        assertEquals("", StringUtils.defaultString(""));
        assertEquals("", StringUtils.defaultString(null));
    }

    @Test
    public void lowerCase() {
        assertEquals("abcdef", StringUtils.lowerCase("abcdef"));
        assertEquals("abcdef", StringUtils.lowerCase("ABCDEF"));
        assertEquals("abc def", StringUtils.lowerCase("ABC DEF"));
    }

    @Test
    public void replaceTemplate() {
        assertEquals("Hello, World!", StringUtils.replaceTemplate("Hello, ${name}!", Map.of("name", "World")));
        assertEquals("Hello, ${name}!", StringUtils.replaceTemplate("Hello, ${name}!", Map.of("na", "World")));
        assertEquals("Hello, !", StringUtils.replaceTemplate("Hello, ${name}!", Map.of("name", "")));
        assertEquals("Hello, !", StringUtils.replaceTemplate("Hello, ${name}!", Map.of("name", "")));
        assertEquals("Hello, ${name}!", StringUtils.replaceTemplate("Hello, ${name}!", new HashMap<>()));
        assertEquals("Hello, World!", StringUtils.replaceTemplate("Hello, $name$!", Map.of("name", "World"), "$", "$"));
        assertEquals("Hello, World!", StringUtils.replaceTemplate("Hello, #name$!", Map.of("name", "World"), "#", "$"));
    }

    @Test
    public void escapeCsv() {
        assertEquals("\"a,b\"", StringUtils.escapeCsv("a,b"));
        assertEquals("abc", StringUtils.escapeCsv("abc"));
        assertEquals("\"a\"\"b\"", StringUtils.escapeCsv("a\"b"));
        assertEquals("\"a\nb\"", StringUtils.escapeCsv("a\nb"));
        assertEquals("\"a\rb\"", StringUtils.escapeCsv("a\rb"));
    }

    @Test
    public void unescapeCsv() {
        assertEquals("a,b", StringUtils.unescapeCsv("\"a,b\""));
        assertEquals("abc", StringUtils.unescapeCsv("abc"));
        assertEquals("a\"b", StringUtils.unescapeCsv("\"a\"\"b\""));
        assertEquals("a\nb", StringUtils.unescapeCsv("\"a\nb\""));
        assertEquals("a\rb", StringUtils.unescapeCsv("\"a\rb\""));
    }
}
