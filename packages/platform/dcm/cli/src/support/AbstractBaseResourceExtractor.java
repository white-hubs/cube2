package com.guhecloud.platform.dcm.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractBaseResourceExtractor implements ResourceExtractor {
    private static final Pattern regexPathPrefixPattern = Pattern.compile("^\\^/([^\\[\\]()*?/]+/)*");
    private static final Pattern antPathPrefixPattern = Pattern.compile("^/([^*?/]+/)*");

    static String extractLongestDeterministicPath(String path, boolean isRegex) {
        // 正则表达式用于匹配以^开始，以/结束的路径部分，不包含正则表达式的特殊字符
        Pattern pattern = isRegex ? regexPathPrefixPattern : antPathPrefixPattern;
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            return matcher.group().substring(1);
        }
        return "";
    }

    /**
     * 将一个类似AntPathPatter 的pattern模式向regex patter转换。
     */
    protected static Pattern convertGlobToRegex(String pattern) {
        StringBuilder sb = new StringBuilder("^");
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            switch (c) {
                case '*':
                    if (i + 1 < pattern.length() && pattern.charAt(i + 1) == '*') {
                        // ** -> 任意级字符串
                        sb.append(".*");
                        i++;
                    } else {
                        sb.append("[^/]*");
                    }
                    break;
                case '?':
                    // 和AntPathPatter有区别： ? 代表0到1个字符而不再是一个字符。
                    sb.append("[^/]?");
                    break;
                case '.':
                    sb.append("\\.");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                default:
                    sb.append(c);
            }
        }
        sb.append("$");
        return Pattern.compile(sb.toString());
    }

    protected static Pattern getRegexPattern(String resourcePattern, boolean isRegex) {
        if (isRegex && (!resourcePattern.startsWith("^") && !resourcePattern.startsWith("/"))) {
            resourcePattern = "^/" + resourcePattern;
        }
        Pattern resourcePatternRegEx = isRegex ? Pattern.compile(resourcePattern) : convertGlobToRegex(resourcePattern);
        return resourcePatternRegEx;
    }
}
