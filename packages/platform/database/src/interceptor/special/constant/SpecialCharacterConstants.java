package com.guhecloud.platform.database.interceptor.special.constant;

public class SpecialCharacterConstants {

    /**
     * SQL statement like
     */
    public final static String SQL_LIKE = "like ";

    /**
     * SQL statement placeholder
     */
    public final static String SQL_PLACEHOLDER = "?";

    /**
     * SQL statement placeholder separated
     */
    public final static String SQL_PLACEHOLDER_REGEX = "\\?";

    /**
     * 需要转移的特殊字符
     */
    public final static String[] ESCAPE_CHAR = new String[]{ "%", "_", "\\" };

    /**
     * like语句样式
     */
    public final static String MYBATIS_PLUS_LIKE_SQL = "like ";

    /**
     * 参数前缀
     */
    public final static String MYBATIS_PLUS_WRAPPER_PREFIX = "ew.paramNameValuePairs.";

    /**
     * 参数键
     */
    public final static String MYBATIS_PLUS_WRAPPER_KEY = "ew";

    /**
     * 参数分隔符
     */
    public final static String MYBATIS_PLUS_WRAPPER_SEPARATOR = ".";

    /**
     * 参数分隔符替换符
     */
    public final static String MYBATIS_PLUS_WRAPPER_SEPARATOR_REGEX = "\\.";

    /**
     * 已更换的标记
     */
    public final static String REPLACED_LIKE_KEYWORD_MARK = "replaced.keyword";

    private SpecialCharacterConstants() {

    }
}
