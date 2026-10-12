package com.guhecloud.platform.base.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public final class BizCodeUtils {
    private BizCodeUtils() {}

    private static final DateTimeFormatter BASIC_DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;

    public static synchronized String generate(String prefix, boolean randomFlag) {
        var code = prefix + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        if (randomFlag) {
            var random = String.format("%04d", new Random().nextInt(9999));
            code = code + "_" + random;
        }
        return code;
    }

    /**
     * 根据业务标识+年月日时分秒+4位随机数得到编码
     *
     * @param prefix 编码前缀
     *
     * @return 生成的编码字符串
     */
    public static synchronized String generate(String prefix) {
        return generate(prefix, true);
    }

    public static synchronized String generate() {
        return generate("");
    }

    /**
     * 根据业务标识+年月日时分秒得到编码
     *
     * @param prefix 编码前缀
     *
     * @return 生成的编码字符串
     */
    public static synchronized String generateNoRandom(String prefix) {
        return generate(prefix, false);
    }

    public static String dailyPrefix(String prefix, LocalDate date, String separator) {
        return prefix + date.format(BASIC_DATE_FORMATTER) + separator;
    }

    public static String nextSequenceCode(String prefix, String currentMaxCode, int sequenceLength) {
        if (sequenceLength < 1 || sequenceLength > 9) {
            throw new IllegalArgumentException("sequenceLength must be between 1 and 9");
        }
        int next = 1;
        if (currentMaxCode != null && currentMaxCode.startsWith(prefix)) {
            String suffix = currentMaxCode.substring(prefix.length());
            next = Integer.parseInt(suffix) + 1;
        }
        int max = (int) Math.pow(10, sequenceLength) - 1;
        if (next > max) {
            throw new IllegalStateException("Daily code sequence exceeded");
        }
        return prefix + String.format("%0" + sequenceLength + "d", next);
    }
}
