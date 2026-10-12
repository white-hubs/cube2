package com.guhecloud.platform.base.util;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.StringSubstitutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class StringUtils {

    public static final String DEFAULT_DELIMITER_REGEX = ",";
    public static final int INDEX_NOT_FOUND = -1;

    private StringUtils() {}

    /**
     * 空字符串
     */
    public static final String EMPTY_STRING = "";

    public static final String[] EMPTY_STRING_ARRAY = {};

    /**
     * 检查字符串数组中是否包含空字符串
     *
     * @param strings 需要检查的字符串数组
     *
     * @return 如果数组中包含空字符串，则返回true；否则返回false
     */
    public static boolean containsEmpty(String... strings) {
        if (strings == null) {
            return false;
        }
        for (String string : strings) {
            if (StringUtils.isEmpty(string)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsBlank(String... strings) {
        if (strings == null) {
            return false;
        }
        for (String string : strings) {
            if (StringUtils.isBlank(string)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(final CharSequence cs) {
        return org.apache.commons.lang3.StringUtils.isEmpty(cs);
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    /**
     * 截取字符串
     *
     * @param original 原始字符串
     * @param index    开始下标（负数标识从字符串末尾开始）
     * @param length   截取长度
     *
     * @return 子字符串
     */
    public static String substring(String original, int index, int length) {
        AssertUtils.assertTrue(length > -1, "Length must be negative or zero to indicate no limit.");
        AssertUtils.assertTrue(original != null, "Original string must not be null.");
        if (index < 0) {
            index += original.length();
        }
        return original.substring(index, Math.min(original.length(), index + length));
    }

    /**
     * 字符串对齐
     *
     * @param original 原始字符串
     * @param length   字符串总长度
     * @param c        补齐字符
     * @param right    是否是右对齐
     *
     * @return 对齐后的字符串
     */
    public static String just(String original, int length, char c, boolean right) {
        AssertUtils.assertTrue(length > -1, "length must be greater than -1");
        original = Optional.ofNullable(original).orElse(EMPTY_STRING);
        var builder = new StringBuilder();
        if (right) {
            builder.append(original);
            for (int i = 0, size = length - original.length(); i < size; i++) {
                builder.append(c);
            }
            return builder.toString();
        }
        for (int i = 0, size = length - original.length(); i < size; i++) {
            builder.append(c);
        }
        builder.append(original);
        return builder.toString();
    }

    /**
     * 截取字符串
     *
     * @param original 原始字符串
     * @param index    开始下标（负数标识从字符串末尾开始）
     *
     * @return 子字符串
     */
    public static String substring(String original, int index) {
        AssertUtils.assertTrue(original != null, "Original string must not be null.");
        return substring(original, index, original.length());
    }

    /**
     * 字符串左对齐
     *
     * @param original 原始字符串
     * @param length   字符串总长度
     *
     * @return 对齐后的字符串
     */
    public static String justLeft(String original, int length) {
        return justLeft(original, length, ' ');
    }

    /**
     * 字符串左对齐
     *
     * @param original 原始字符串
     * @param length   字符串总长度
     * @param c        补齐字符
     *
     * @return 对齐后的字符串
     */
    public static String justLeft(String original, int length, char c) {
        return StringUtils.just(original, length, c, false);
    }

    /**
     * 字符串右对齐
     *
     * @param original 原始字符串
     * @param length   字符串总长度
     *
     * @return 对齐后的字符串
     *
     * @deprecated 不合理的工具方法，省略了补齐字符参数，会导致可读性降低
     */
    @Deprecated
    public static String justRight(String original, int length) {
        return justRight(original, length, ' ');
    }

    /**
     * 字符串右对齐
     *
     * @param original 原始字符串
     * @param length   字符串总长度
     * @param c        补齐字符
     *
     * @return 对齐后的字符串
     */
    public static String justRight(String original, int length, char c) {
        return StringUtils.just(original, length, c, true);
    }

    /**
     * 检查字符串是否为空或仅包含空白字符
     *
     * @param string 需要检查的字符串
     *
     * @return 如果字符串不为空且不包含空白字符，则返回true；否则返回false
     */
    public static boolean isNotBlank(final CharSequence string) {
        return !isBlank(string);
    }

    public static boolean isAnyBlank(final CharSequence... css) {
        if (css == null || css.length == 0) {
            return true; // 如果数组为空或长度为0，认为是空白
        }
        for (final CharSequence cs : css) {
            if (isBlank(cs)) {
                return true; // 只要有一个元素是空白，就返回true
            }
        }
        return false; // 所有元素都不是空白
    }

    public static boolean isNoneBlank(final CharSequence... css) {
        return !isAnyBlank(css);
    }

    public static boolean isBlank(final CharSequence string) {
        return org.apache.commons.lang3.StringUtils.isBlank(string);
    }

    public static String join(String delimiter, final Object... objects) {
        return org.apache.commons.lang3.StringUtils.join(objects, delimiter);
    }

    public static String join(String delimiter, final Iterable<?> objects) {
        return org.apache.commons.lang3.StringUtils.join(objects, delimiter);
    }

    public static <T> List<T> splitAndTranslate(String string, Function<String, T> transformer, String delimiterRegex) {
        if (string == null) {
            return Collections.emptyList();
        } else {
            String[] parts = string.split(delimiterRegex);
            return Arrays.stream(parts)
                    .map(transformer)
                    .collect(Collectors.toList());
        }
    }

    public static <T> List<T> splitAndTranslate(String string, Function<String, T> transformer) {
        return splitAndTranslate(string, transformer, DEFAULT_DELIMITER_REGEX);
    }

    public static List<String> split(String string, char delimiter) {
        return split(string, String.valueOf(delimiter));
    }

    public static List<String> split(String string, String delimiterRegex) {
        if (isEmpty(string)) {
            return Collections.emptyList();
        }
        String[] parts = string.split(delimiterRegex);
        return Arrays.stream(parts).collect(Collectors.toList());
    }

    public static List<String> split(String string) {
        return split(string, DEFAULT_DELIMITER_REGEX);
    }

    public static List<String> splitAndIgnoreBlank(String string, String delimiterRegex) {
        if (string == null) {
            return Collections.emptyList();
        }
        String[] parts = string.split(delimiterRegex);
        return Arrays.stream(parts)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
    }

    /**
     * 将字符串按默认分隔符切割，并忽略空字符串
     *
     * @param string 需要切割的字符串
     *               delimiterRegex 分隔符正则表达式，默认为逗号
     *
     * @return 切割后的字符串列表，空字符串将被忽略
     */
    public static List<String> splitAndIgnoreBlank(String string) {
        return splitAndIgnoreBlank(string, DEFAULT_DELIMITER_REGEX);
    }

    /**
     * 占位符替换 ${key} -> value
     *
     * @param str 源字符串
     * @param map 替换参数Map
     *
     * @return
     *
     * @deprecated use {@link StringUtils.replaceTemplate(String, Map)} instead
     */
    @Deprecated
    public static String placeholderReplace(String str, Map<String, String> map) {
        if (str.contains("$")) {
            String patternString = "\\$\\s*\\{\\s*(.+?)\\s*\\}";
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(str);
            String text = "";
            while (matcher.find()) {
                String keyString = matcher.group(1);
                String value = map.get(keyString);
                String replaceValue = Matcher.quoteReplacement(value);
                text = matcher.replaceFirst(replaceValue);
                matcher = pattern.matcher(text);
            }
            return text;
        }
        return str;
    }

    public static boolean equalsAny(final CharSequence string, final CharSequence... searchStrings) {
        if (searchStrings == null || string == null) {
            return false;
        }
        for (final CharSequence next : searchStrings) {
            if (string.equals(next)) {
                return true;
            }
        }
        return false;
    }

    public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }
        if (cs1 instanceof String && cs2 instanceof String) {
            return cs1.equals(cs2);
        }
        // Step-wise comparison
        final int length = cs1.length();
        for (int i = 0; i < length; i++) {
            if (cs1.charAt(i) != cs2.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static boolean contains(final String seq, final CharSequence searchSeq) {
        if (seq == null || searchSeq == null) {
            return false;
        }
        return seq.contains(searchSeq);
    }

    public static boolean endsWith(String string, String s) {
        if (string == null || s == null) {
            return Objects.equals(string, s);
        }
        return string.endsWith(s);
    }

    public static String substringBefore(final String str, final String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.isEmpty()) {
            return EMPTY_STRING;
        }
        final int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static String capitalize(final String str) {
        final int strLen = Optional.ofNullable(str).orElse(EMPTY_STRING).length();
        if (strLen == 0) {
            return str;
        }

        final int firstCodepoint = str.codePointAt(0);
        final int newCodePoint = Character.toTitleCase(firstCodepoint);
        if (firstCodepoint == newCodePoint) {
            // already capitalized
            return str;
        }

        final int[] newCodePoints = new int[strLen]; // cannot be longer than the char array
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint; // copy the first code point
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; ) {
            final int codePoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codePoint; // copy the remaining ones
            inOffset += Character.charCount(codePoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }

    /**
     * 检查CharSequence是否仅包含Unicode数字。
     * 小数点不是Unicode数字，返回false
     *
     * @param cs CharSequence to check
     *
     * @return 如果CharSequence仅包含Unicode数字，则返回true；否则返回false
     */
    public static boolean isNumeric(final CharSequence cs) {
        return org.apache.commons.lang3.StringUtils.isNumeric(cs);
    }

    /**
     * 检查CharSequence是否仅包含Unicode数字或空格。
     *
     * @param cs CharSequence to check
     *
     * @return 如果CharSequence仅包含Unicode数字或空格，则返回true；否则返回false
     */
    public static boolean isNumericSpace(final CharSequence cs) {
        return org.apache.commons.lang3.StringUtils.isNumericSpace(cs);
    }

    public static boolean isNotNumericSpace(final CharSequence cs) {
        return !isNumericSpace(cs);
    }

    /**
     * 检查字符串是否以指定前缀开头，忽略大小写。
     *
     * @param str    需要检查的字符串
     * @param prefix 前缀字符串
     *
     * @return 如果字符串以指定前缀开头（忽略大小写），则返回true；否则返回false
     */
    public static boolean startsWithIgnoreCase(final CharSequence str, final CharSequence prefix) {
        return org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(str, prefix);
    }

    /**
     * 检查CharSequence是否包含任何指定的字符。
     *
     * @param cs          需要检查的CharSequence
     * @param searchChars 包含的字符序列
     *
     * @return 如果CharSequence包含任何指定的字符，则返回true；否则返回false
     */
    public static boolean containsAny(final CharSequence cs, final CharSequence searchChars) {
        return org.apache.commons.lang3.StringUtils.containsAny(cs, searchChars);
    }

    /**
     * 将数组中的元素连接成一个字符串，使用指定的分隔符。
     *
     * @param delimiter 分隔符
     * @param array     需要连接的对象数组
     *
     * @return 连接后的字符串
     */
    public static String joinWith(final String delimiter, final Object... array) {
        return org.apache.commons.lang3.StringUtils.joinWith(delimiter, array);
    }

    /**
     * 比较两个CharSequence是否相等，忽略大小写。
     *
     * @param cs1 第一个CharSequence
     * @param cs2 第二个CharSequence
     *
     * @return 如果两个CharSequence相等（忽略大小写），则返回true；否则返回false
     */
    public static boolean equalsIgnoreCase(final CharSequence cs1, final CharSequence cs2) {
        return org.apache.commons.lang3.StringUtils.equalsIgnoreCase(cs1, cs2);
    }

    public static String toCamelCase(final CharSequence name) {
        return toCamelCase(name, '_');
    }

    public static String toCamelCase(final CharSequence name, final char symbol) {
        if (null == name) {
            return null;
        }

        final String name2 = name.toString();
        if (contains(name2, String.valueOf(symbol))) {
            final int length = name2.length();
            final StringBuilder sb = new StringBuilder(length);
            boolean upperCase = false;
            for (int i = 0; i < length; i++) {
                char c = name2.charAt(i);

                if (c == symbol) {
                    upperCase = true;
                } else if (upperCase) {
                    sb.append(Character.toUpperCase(c));
                    upperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
            return sb.toString();
        } else {
            return name2;
        }
    }

    /**
     * 重复字符串
     *
     * @param str   字符串
     * @param count 重复次数
     *
     * @return 重复后的字符串
     */
    public static String repeat(CharSequence str, int count) {
        if (null == str) {
            return null;
        }
        if (count <= 0 || str.isEmpty()) {
            return EMPTY_STRING;
        }
        if (count == 1) {
            return str.toString();
        }

        // 检查
        final int len = str.length();
        final long longSize = (long) len * (long) count;
        final int size = (int) longSize;
        if (size != longSize) {
            throw new ArrayIndexOutOfBoundsException("Required String length is too large: " + longSize);
        }

        final char[] array = new char[size];
        str.toString().getChars(0, len, array, 0);
        int n;
        for (n = len; n < size - n; n <<= 1) {// n <<= 1相当于n *2
            System.arraycopy(array, 0, array, n, n);
        }
        System.arraycopy(array, 0, array, n, size - n);
        return new String(array);
    }

    public static String defaultIfEmpty(String s, String defaultString) {
        if (isEmpty(s)) {
            return defaultString;
        }
        return s;
    }

    public static String defaultIfBlank(String s, String defaultString) {
        if (isBlank(s)) {
            return defaultString;
        }
        return s;
    }

    public static String defaultString(String s) {
        return Objects.toString(s, EMPTY_STRING);
    }

    public static String lowerCase(String s) {
        if (s == null) {
            return null;
        }
        return s.toLowerCase();
    }

    public static String replaceTemplate(String content, Map<String, String> params) {
        return replaceTemplate(content, params, "${", "}");
    }

    public static String replaceTemplate(String content, Map<String, String> params, String prefix, String suffix) {
        var strSubstitutor = new StringSubstitutor(params, prefix, suffix);
        return strSubstitutor.replace(content);
    }

    public static String escapeCsv(String s) {
        return StringEscapeUtils.escapeCsv(s);
    }

    public static String unescapeCsv(String s) {
        return StringEscapeUtils.unescapeCsv(s);
    }

    /**
     * 使用指定的分隔符将字符串切割成字符串数组。
     * impl copy from org.springframework.util.StringUtils
     *
     * @param str               需要切割的字符串
     * @param delimiters        分隔符字符串
     * @param trimTokens        是否去除每个切割后的子字符串的前后空白
     * @param ignoreEmptyTokens 是否忽略空字符串
     *
     * @return 切割后的字符串数组
     */
    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        if (str == null) {
            return EMPTY_STRING_ARRAY;
        }

        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || !token.isEmpty()) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    private static String[] toStringArray(Collection<String> collection) {
        return (collection != null && !collection.isEmpty()) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY;
    }
}
