package com.guhecloud.platform.base.util;

import cn.hutool.core.util.RandomUtil;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.commons.text.TextRandomProvider;

import java.security.SecureRandom;

public class RandomUtils {

    private static final RandomStringGenerator ALPHABETIC_GENERATOR =
            new RandomStringGenerator.Builder()
                    .withinRange(new char[][]{ { 'a', 'z' }, { 'A', 'Z' } })
                    .usingRandom(new SecureRandomProvider())
                    .get();

    private RandomUtils() {}

    /**
     * 生成一个随机字符串，其长度为指定的字符数。
     *
     * @param length 待创建随机字符串的长度
     *
     * @return 随机字符串
     */
    public static String randomAlphabetic(final int length) {
        return ALPHABETIC_GENERATOR.generate(length);
    }

    private static class SecureRandomProvider implements TextRandomProvider {
        private final SecureRandom secureRandom;

        public SecureRandomProvider() {
            this.secureRandom = new SecureRandom();
            this.secureRandom.nextInt();
        }

        @Override
        public int nextInt(int bound) {
            return secureRandom.nextInt(bound);
        }
    }

    /**
     * 获得一个只包含数字的字符串
     *
     * @param length 字符串的长度
     *
     * @return 随机字符串
     */
    public static String randomNumbers(int length) {
        return RandomUtil.randomNumbers(length);
    }

    public static char randomNumbers() {
        return RandomUtil.randomNumber();
    }
}
