package com.guhecloud.platform.base.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class RegexUtils {
    private RegexUtils() {}

    /**
     * 英文字母正则表达式
     */
    public static final String LETTER_REGEX = "[a-zA-Z]+";

    /**
     * 整数正则表达式
     */
    public static final String INTEGER_REGEX = "-?[0-9]+";

    /**
     * 逗号拆分分隔符
     */
    public static final String COMMA_SPLIT_DELIMITER = "[ ,]";

    /**
     * 数字正则表达式
     */
    public static final String NUMBER_REGEX = "-?(\\.?[0-9]+|[0-9]+\\.?[0-9]+|[0-9]+\\.?)";

    /**
     * URL正则表达式
     */
    public static final String URL_REGEX = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 邮箱正则表达式
     */
    public static final String EMAIL_REGEX = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

    /**
     * URL正则表达式匹配模式
     */
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    /**
     * 邮箱正则表达式匹配模式
     */
    public static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * 英文字母正则表达式匹配模式
     */
    private static final Pattern LETTER_PATTERN = Pattern.compile(LETTER_REGEX);

    /**
     * 整数正则表达式匹配模式
     */
    private static final Pattern INTEGER_PATTERN = Pattern.compile(INTEGER_REGEX);

    /**
     * 数字正则表达式匹配模式
     */
    private static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER_REGEX);

    /**
     * 正则表达式/匹配模式对象映射表
     */
    private static final Map<String, Pattern> REGEX_PATTERN_MAPPING = new ConcurrentHashMap<>();

    /**
     * 获取正则表达式匹配模式
     *
     * @param regex 正则表达式
     *
     * @return 匹配模式对象
     */
    public static Pattern getPattern(String regex) {
        return REGEX_PATTERN_MAPPING.computeIfAbsent(regex, k -> Pattern.compile(regex));
    }

    /**
     * 正则表达式匹配
     *
     * @param source 源字符串
     * @param regex  正则表达式
     *
     * @return true/false
     */
    public static boolean matches(CharSequence source, String regex) {
        return source != null && getPattern(regex).matcher(source).matches();
    }

    /**
     * 判断字符串是否是英文字母
     *
     * @param source 字符串
     *
     * @return true/false
     */
    public static boolean isLetter(CharSequence source) {
        return source != null && LETTER_PATTERN.matcher(source).matches();
    }

    /**
     * 判断字符串是否是URL
     *
     * @param source 字符串
     *
     * @return true/false
     */
    public static boolean isUrl(CharSequence source) {
        return source != null && URL_PATTERN.matcher(source).matches();
    }

    /**
     * 判断字符串是否是整数
     *
     * @param source 字符串
     *
     * @return true/false
     */
    public static boolean isInteger(CharSequence source) {
        return source != null && INTEGER_PATTERN.matcher(source).matches();
    }

    /**
     * 判断字符串是否是数字
     *
     * @param source 字符串
     *
     * @return true/false
     */
    public static boolean isNumber(CharSequence source) {
        return source != null && NUMBER_PATTERN.matcher(source).matches();
    }

    /**
     * 正则表达式替换
     *
     * @param source      源字符串
     * @param regex       正则表达式
     * @param replacement 替换字符串
     *
     * @return 替换后字符串
     */
    public static String replace(CharSequence source, String regex, String replacement) {
        return source == null ? null : replacement == null ? source.toString() :
                                       getPattern(regex).matcher(source).replaceAll(replacement);
    }

    /**
     * 字符串拆分
     *
     * @param source 被拆分字符串
     * @param regex  分割符正则表达式
     *
     * @return 拆分字符串数组
     */
    public static String[] split(CharSequence source, String regex) {
        return source == null ? null : getPattern(regex).split(source);
    }

    /**
     * 字符串拆分
     *
     * @param source 被拆分字符串
     * @param regex  分割符正则表达式
     * @param limit  开始下标
     *
     * @return 拆分字符串数组
     */
    public static String[] split(CharSequence source, String regex, int limit) {
        return source == null ? null : getPattern(regex).split(source, limit);
    }

    /**
     * 拆分有效字符串（默认使用","号拆分）
     *
     * @param source 被拆分字符串
     *
     * @return 字符串拆分数组
     */
    public static String[] splitActives(CharSequence source) {
        return splitActives(source, COMMA_SPLIT_DELIMITER);
    }

    /**
     * 拆分有效字符串（忽略空格）
     *
     * @param regex  拆分符号正则表达式
     * @param source 被拆分字符串
     *
     * @return 字符串拆分数组
     */
    public static String[] splitActives(CharSequence source, String regex) {
        return source == null ? null :
               Stream.of(split(source, regex)).filter(s -> !StringUtils.isEmpty(s)).toArray(String[]::new);
    }
}
