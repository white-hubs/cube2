package com.guhecloud.platform.base.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegexUtilsTests {
    @Test
    public void getPattern() {
        var pattern = RegexUtils.getPattern("^a$");
        assertTrue(pattern.matcher("a").matches());
    }

    @Test
    public void matches() {
        assertTrue(RegexUtils.matches("a", "^a$"));
    }

    @Test
    public void isLetter() {
        assertTrue(RegexUtils.isLetter("a"));
        assertFalse(RegexUtils.isLetter("1a"));
    }

    @Test
    public void isUrl() {
        assertTrue(RegexUtils.isUrl("http://www.example.com"));
        assertTrue(RegexUtils.isUrl("https://www.example.com/path?query=1"));
        assertTrue(RegexUtils.isUrl("http://example.com"));
        assertTrue(RegexUtils.isUrl("http://192.168.0.1"));
        assertFalse(RegexUtils.isUrl("ws://example.com"));
        assertFalse(RegexUtils.isUrl("1a"));
    }

    @Test
    public void isInteger() {
        assertTrue(RegexUtils.isInteger("1"));
        assertTrue(RegexUtils.isInteger("-1"));
        assertFalse(RegexUtils.isInteger("1.1"));
        assertFalse(RegexUtils.isInteger("1a"));
    }

    @Test
    public void isNumber() {
        assertTrue(RegexUtils.isNumber("1"));
        assertTrue(RegexUtils.isNumber("-1"));
        assertTrue(RegexUtils.isNumber("1.1"));
        assertTrue(RegexUtils.isNumber("-1.1"));
        assertFalse(RegexUtils.isNumber("1.1.1"));
        assertFalse(RegexUtils.isNumber("1a"));
    }

    @Test
    public void replace() {
        assertEquals("abc", RegexUtils.replace("a", "^a$", "abc"));
    }

    @Test
    public void split() {
        assertEquals("c", RegexUtils.split("abcabeabd", ".b")[1]);
        assertEquals("abcabeabd", RegexUtils.split("abcabeabd", ".b", 1)[0]);
    }

    @Test
    public void splitActives() {
        assertEquals("e", RegexUtils.splitActives("abcabeabd", ".b")[1]);
        assertEquals("abcabeabd", RegexUtils.split("abcabeabd", ".b", 1)[0]);
    }
}
