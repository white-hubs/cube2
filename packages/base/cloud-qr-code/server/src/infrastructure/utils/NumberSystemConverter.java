package com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.utils;

/**
 * 进制转换工具
 * 用于将长整型ID转换为62进制的短字符串
 */
public class NumberSystemConverter {

    private static final char[] CHAR_MAP = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final int SCALE = 62;

    /**
     * 将长整型数字转换为62进制字符串
     *
     * @param number 数字
     *
     * @return 62进制字符串
     */
    public static String toBase62(long number) {
        if (number == 0) {
            return "0";
        }

        var sb = new StringBuilder();
        var num = number;
        // 取模运算，获取对应字符
        while (num > 0) {
            var remainder = (int) (num % SCALE);
            sb.append(CHAR_MAP[remainder]);
            num = num / SCALE;
        }

        return sb.reverse().toString();
    }

    /**
     * 将长整型数字转换为固定长度的62进制字符串，不足左边补0
     *
     * @param number 数字
     * @param length 目标长度
     *
     * @return 固定长度的62进制字符串
     */
    public static String toBase62(long number, int length) {
        var base62 = toBase62(number);
        if (base62.length() >= length) {
            return base62;
        }
        // 不足长度，左边补 '0'
        return "0".repeat(length - base62.length()) +
               base62;
    }
}

