package com.guhecloud.cube.deliver.project.merchant.commons.utils;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.NonNull;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 数据脱敏工具类
 * <p>
 * ***-  : 对前三个字符脱敏
 * -***  : 对后三个字符串脱敏
 * -***- : 对中间三个字符脱敏
 * -***-?: 从第二个字符开始的三个字符脱敏
 * -?***-: 从倒数第二个字符开始的三个字符脱敏
 * *.2-  : 对前2/10个字符脱敏
 * -*.2  : 对后2/10个字符脱敏
 * -*.2- : 对中间2/10个字符脱敏
 * -*.2-?: 从第二个字符开始对对前2/10个字符脱敏
 * -?*.2-: 从倒数第二个字符开始对对后2/10个字符脱敏
 * --*?  : 从第三个字符开始脱敏
 * *?--  : 从倒数第三个字符开始脱敏
 * --*?- : 从左边第三个、右边倒数第二个字符开始脱敏
 *
 * @author Woody
 * @date 2021/7/11
 */
public final class DesensitizeUtils {
    /**
     * 脱敏信息符号
     */
    public static final char SYMBOL = '*';

    /**
     * 固定数量匹配模式
     */
    private static final Pattern FIXED_PATTERN = Pattern.compile("^(-*|-\\?)\\*+(-*|-\\?)$");

    /**
     * 占比数量匹配模式
     */
    private static final Pattern RATIO_PATTERN = Pattern.compile("^(-*|-\\?)\\*\\.[1-9](-*|-\\?)$");

    /**
     * 反向数量匹配模式
     */
    private static final Pattern REVERSE_PATTERN = Pattern.compile("^-*\\*\\?-*$");

    private DesensitizeUtils() {
    }

    /**
     * 复制脱敏符号
     *
     * @param length 符号长度
     * @return 脱敏符号字符串
     */
    private static String duplicateSymbols(int length) {
        Assert.isTrue(length > -1, "length must be greater than -1");
        if (length == 0) {
            return "";
        }
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = SYMBOL;
        }
        return new String(chars);
    }

    /**
     * 获取偏移量，负数标识从后往前偏移
     *
     * @param expression 脱敏表达式
     * @return 偏移量
     */
    private static int getOffset(String expression) {
        boolean startsWith = expression.startsWith("-?"), endsWith = expression.endsWith("-?");
        if (startsWith && !endsWith) {
            char[] chars = expression.toCharArray();
            int i = chars.length - 1;
            for (; i >= 0 && chars[i] == '-'; i--) ;
            return -(chars.length - i - 1);
        } else if (endsWith && !startsWith) {
            return expression.substring(0, expression.indexOf(SYMBOL)).length();
        }
        return 0;
    }

    /**
     * 脱敏表达式解析
     *
     * @param expression 脱敏表达式
     * @return 脱敏模式对象
     */
    public static Mode analyse(@NonNull String expression) {
        if (FIXED_PATTERN.matcher(expression).matches()) {
            int number = RegexUtils.replace(expression, "[-?]", "").length();
            return new Fixed(Position.of(expression), number, getOffset(expression));
        } else if (RATIO_PATTERN.matcher(expression).matches()) {
            String number = RegexUtils.replace(expression, "[-*?]", "");
            return new Ratio(Position.of(expression), Float.parseFloat(number), getOffset(expression));
        } else if (REVERSE_PATTERN.matcher(expression).matches()) {
            int index = expression.indexOf("*?");
            return new Reverse(index, expression.length() - index - 2);
        }
        throw new IllegalArgumentException("Invalid expression: " + expression);
    }

    /**
     * 敏感数据脱敏
     *
     * @param source      源数据
     * @param replacement 脱敏表达式
     * @return 脱敏后数据
     */
    public static String desensitize(String source, String replacement) {
        return StringUtils.isEmpty(source) || StringUtils.isEmpty(replacement) ? source :
               Objects.requireNonNull(analyse(replacement)).desensitize(source);
    }

    /**
     * 敏感数据位置
     */
    public enum Position {
        /**
         * 开始位置
         */
        START,

        /**
         * 结束位置
         */
        END,

        /**
         * 中间位置
         */
        MIDDLE;

        /**
         * 根据脱敏表达式获取敏感数据位置
         *
         * @param expression 脱敏表达式
         * @return 位置枚举
         */
        private static Position of(String expression) {
            char first = expression.charAt(0);
            char last = expression.charAt(expression.length() - 1);
            if (first == '-' && (last == '-' || last == '?')) {
                return MIDDLE;
            } else if (first == '-') {
                return END;
            }
            return START;
        }
    }

    /**
     * 脱敏模式
     */
    public interface Mode {
        /**
         * 数据脱敏
         *
         * @param source 敏感数据
         * @return 脱敏数据
         */
        String desensitize(String source);
    }

    /**
     * 脱敏目标数据索引
     */
    public static class Index {
        /**
         * 开始下标
         */
        private final int start;

        /**
         * 结束下标
         */
        private final int ent;

        public Index(int start, int end) {
            Assert.isTrue(start > -1, "start must be greater than -1");
            Assert.isTrue(end > -1, "end must be greater than -1");
            this.start = start;
            this.ent = end;
        }
    }

    /**
     * 数据脱敏抽象类
     */
    public static abstract class AbstractMode implements Mode {
        /**
         * 获取脱敏目标数据索引位置
         *
         * @param source 敏感数据
         * @return 脱敏目标数据索引位置
         */
        protected abstract Index getIndex(String source);

        @Override
        public String desensitize(String source) {
            if (StringUtils.isEmpty(source)) {
                return source;
            }
            Index index = Objects.requireNonNull(this.getIndex(source));
            if (index.ent == 0 || index.ent <= index.start) {
                return source;
            }
            String desensitized = source.substring(0, index.start);
            desensitized = desensitized.concat(duplicateSymbols(index.ent - index.start));
            if (index.ent < source.length()) {
                desensitized = desensitized.concat(source.substring(index.ent));
            }
            return desensitized;
        }
    }

    /**
     * 固定数量脱敏实现
     */
    public static class Fixed extends AbstractMode {
        /**
         * 目标脱敏数据位置
         */
        private final Position position;

        /**
         * 脱敏数量
         */
        private final int number;

        /**
         * 偏移量
         */
        private final int offset;

        public Fixed(@NonNull Position position, int number, int offset) {
            Assert.isTrue(number > 0, "number must be greater than 0");
            this.position = position;
            this.number = number;
            this.offset = offset;
        }

        @Override
        protected Index getIndex(@NonNull String source) {
            if (this.position == Position.START) {
                return new Index(0, Math.min(this.number, source.length()));
            } else if (this.position == Position.END) {
                return new Index(Math.max(0, source.length() - this.number), source.length());
            } else if (this.offset > 0) {
                return new Index(Math.min(source.length(), this.offset),
                        Math.min(source.length(), this.offset + this.number));
            } else if (this.offset < 0) {
                return new Index(Math.min(source.length(), Math.max(0, source.length() + this.offset - this.number)),
                        source.length() + this.offset);
            }
            int index = source.length() / 2;
            int offset = this.number / 2;
            return new Index(Math.max(0, index - offset), Math.min(index + this.number - offset, source.length()));
        }
    }

    /**
     * 占比数量脱敏实现
     */
    public static class Ratio extends AbstractMode {
        /**
         * 目标脱敏数据位置
         */
        private final Position position;

        /**
         * 脱敏数量
         */
        private final float number;

        /**
         * 偏移量
         */
        private final int offset;

        public Ratio(@NonNull Position position, float number, int offset) {
            Assert.isTrue(number > 0, "number must be greater than 0");
            this.position = position;
            this.number = number;
            this.offset = offset;
        }

        @Override
        protected Index getIndex(@NonNull String source) {
            if (this.position == Position.START) {
                return new Index(0, (int) Math.ceil(source.length() * this.number));
            } else if (this.position == Position.END) {
                return new Index((int) Math.floor(source.length() * (1 - this.number)), source.length());
            } else if (this.offset > 0) {
                return new Index(Math.min(source.length(), this.offset),
                        Math.min(source.length(), this.offset + (int) Math.ceil(source.length() * this.number)));
            } else if (this.offset < 0) {
                return new Index(Math.min(source.length(),
                        Math.max(0, source.length() + this.offset - (int) Math.ceil(source.length() * this.number))),
                        source.length() + this.offset);
            }
            int size = (int) Math.ceil(source.length() * this.number);
            int index = source.length() / 2;
            int offset = size / 2;
            return new Index(Math.max(0, index - offset), Math.min(index + size - offset, source.length()));
        }
    }

    /**
     * 反向数量脱敏实现
     */
    public static class Reverse extends AbstractMode {
        /**
         * 目标脱敏数据前置明文数量
         */
        private final int before;

        /**
         * 目标脱敏数据后置明文数量
         */
        private final int after;

        public Reverse(int before, int after) {
            Assert.isTrue(before > -1, "before must be greater than -1");
            Assert.isTrue(after > -1, "after must be greater than -1");
            this.before = before;
            this.after = after;
        }

        @Override
        protected Index getIndex(@NonNull String source) {
            return new Index(Math.min(source.length(), this.before), Math.max(0, source.length() - this.after));
        }
    }
}
