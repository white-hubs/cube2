package com.guhecloud.platform.base.util;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class RandomUtilsTests {

    // 验证生成的字符串只包含字母
    private static final Pattern ALPHABETIC_PATTERN = Pattern.compile("^[a-zA-Z]+$");

    @Test
    public void testRandomAlphabeticLength() {
        // 测试不同长度的字符串生成
        for (int length : new int[]{ 1, 5, 10, 20, 50, 100 }) {
            String result = RandomUtils.randomAlphabetic(length);
            assertEquals("生成的字符串长度应为 " + length, length, result.length());
        }
    }

    @Test
    public void testRandomAlphabeticInvalidLength() {
        assertThrows("无效长度应抛出 IllegalArgumentException", IllegalArgumentException.class,
                     () -> RandomUtils.randomAlphabetic(-1));
        assertThrows("无效长度应抛出 IllegalArgumentException", IllegalArgumentException.class,
                     () -> RandomUtils.randomAlphabetic(-5));
        assertThrows("无效长度应抛出 IllegalArgumentException", IllegalArgumentException.class,
                     () -> RandomUtils.randomAlphabetic(-100));
    }

    @Test
    public void testRandomAlphabeticContent() {
        // 验证生成的字符串只包含字母
        for (int i = 0; i < 100; i++) {
            String result = RandomUtils.randomAlphabetic(10);
            assertTrue("生成的字符串应只包含字母: " + result, ALPHABETIC_PATTERN.matcher(result).matches());
        }
    }

    @Test
    public void testRandomAlphabeticUniqueness() {
        // 验证多次调用生成不同结果
        Set<String> generatedStrings = new HashSet<>();
        int sampleSize = 100;

        for (int i = 0; i < sampleSize; i++) {
            String result = RandomUtils.randomAlphabetic(10);
            generatedStrings.add(result);
        }

        // 允许少量重复（因为随机性），但大部分应该是唯一的
        assertTrue("生成的字符串应有足够的唯一性", generatedStrings.size() > sampleSize * 0.9);
    }

    @Test
    public void testRandomAlphabeticCaseSensitivity() {
        // 验证同时包含大小写字母
        boolean hasLower = false;
        boolean hasUpper = false;

        for (int i = 0; i < 100; i++) {
            String result = RandomUtils.randomAlphabetic(5);
            if (result.matches(".*[a-z].*")) {
                hasLower = true;
            }
            if (result.matches(".*[A-Z].*")) {
                hasUpper = true;
            }
            if (hasLower && hasUpper) {
                break;
            }
        }
        assertTrue("应生成小写字母", hasLower);
        assertTrue("应生成大写字母", hasUpper);
    }

    @Test
    public void testConstructorPrivate() {
        // 验证工具类构造函数私有化
        assertThrows("工具类构造函数应为私有", IllegalAccessException.class,
                     () -> RandomUtils.class.getDeclaredConstructor().newInstance());
    }

    @Test
    public void randomNumbers() {
        assertEquals(1, RandomUtils.randomNumbers(1).length());
        assertEquals(13, RandomUtils.randomNumbers(13).length());
        assertTrue("1234567890".contains(String.valueOf(RandomUtils.randomNumbers())));
    }

}