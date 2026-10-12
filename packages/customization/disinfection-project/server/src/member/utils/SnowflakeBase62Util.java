package com.guhecloud.cube.srv.gen.disinfection.server.member.utils;

/**
 * Snowflake ID 转 Base62 编码工具类
 * 用于将 Snowflake ID 转换为 Base62 格式的段密码
 *
 * @author guhecloud
 */
public class SnowflakeBase62Util {

    /**
     * Base62 字符集：包含数字、小写字母、大写字母
     */
    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Base62 进制基数
     */
    private static final int BASE = 62;

    /**
     * 将 Snowflake ID 转换为 Base62 编码的段密码
     *
     * @param snowflakeId Snowflake ID (long 类型)
     * @return Base62 编码的字符串
     */
    public static String encode(long snowflakeId) {
        if (snowflakeId == 0) {
            return "0";
        }

        StringBuilder result = new StringBuilder();
        long num = Math.abs(snowflakeId); // 处理负数情况

        while (num > 0) {
            result.append(BASE62_CHARS.charAt((int) (num % BASE)));
            num /= BASE;
        }

        // 如果原数是负数，添加前缀标识
        if (snowflakeId < 0) {
            result.append('-');
        }

        return result.reverse().toString();
    }

    /**
     * 将 Base62 编码的段密码解码为 Snowflake ID
     *
     * @param base62String Base62 编码的字符串
     * @return 解码后的 Snowflake ID
     * @throws IllegalArgumentException 如果输入字符串包含非法字符
     */
    public static long decode(String base62String) {
        if (base62String == null || base62String.isEmpty()) {
            throw new IllegalArgumentException("Base62 字符串不能为空");
        }

        if ("0".equals(base62String)) {
            return 0;
        }

        boolean isNegative = false;
        String processStr = base62String;

        // 检查是否为负数
        if (base62String.startsWith("-")) {
            isNegative = true;
            processStr = base62String.substring(1);
        }

        long result = 0;
        long power = 1;

        // 从右到左处理每个字符
        for (int i = processStr.length() - 1; i >= 0; i--) {
            char ch = processStr.charAt(i);
            int index = BASE62_CHARS.indexOf(ch);

            if (index == -1) {
                throw new IllegalArgumentException("包含非法字符: " + ch);
            }

            result += index * power;
            power *= BASE;
        }

        return isNegative ? -result : result;
    }

    /**
     * 生成指定长度的 Base62 编码字符串（用于固定长度的段密码）
     *
     * @param snowflakeId Snowflake ID
     * @param minLength   最小长度，不足时左侧补 '0'
     * @return 指定长度的 Base62 编码字符串
     */
    public static String encodeWithPadding(long snowflakeId, int minLength) {
        String encoded = encode(snowflakeId);

        if (encoded.length() >= minLength) {
            return encoded;
        }

        StringBuilder result = new StringBuilder();
        boolean isNegative = encoded.startsWith("-");
        String numberPart = isNegative ? encoded.substring(1) : encoded;

        // 补齐长度
        int paddingLength = minLength - numberPart.length();
        if (isNegative) {
            paddingLength -= 1; // 减去负号占用的长度
        }

        if (isNegative) {
            result.append('-');
        }

        result.append("0".repeat(Math.max(0, paddingLength)));

        result.append(numberPart);
        return result.toString();
    }

    /**
     * 验证字符串是否为有效的 Base62 编码
     *
     * @param base62String 待验证的字符串
     * @return true 如果是有效的 Base62 编码，否则返回 false
     */
    public static boolean isValidBase62(String base62String) {
        if (base62String == null || base62String.isEmpty()) {
            return false;
        }

        String processStr = base62String;

        // 处理负数情况
        if (base62String.startsWith("-")) {
            if (base62String.length() == 1) {
                return false; // 只有负号是无效的
            }
            processStr = base62String.substring(1);
        }

        // 检查每个字符是否在 Base62 字符集中
        for (char ch : processStr.toCharArray()) {
            if (BASE62_CHARS.indexOf(ch) == -1) {
                return false;
            }
        }

        return true;
    }

    public static String shortId() {
        return encode(SnowflakeIdGenerator.getInstance().nextId());
    }

    public static void main(String[] args) {
        long id = SnowflakeIdGenerator.getInstance().nextId();
        String base62 = encode(id);
        long decodedId = decode(base62);

        System.out.println("Original ID: " + id);
        System.out.println("Base62 Encoded: " + base62);
        System.out.println("Decoded ID: " + decodedId);
        System.out.println("Is valid Base62: " + isValidBase62(base62));
        System.out.println("Encoded with padding (length 10): " + encodeWithPadding(id, 10));

        System.out.println(
                "======================================================================================================");
        String shortId = shortId();
        System.out.println("Short ID: " + shortId);
        System.out.println("Decoded Short ID: " + decode(shortId));
    }
}
