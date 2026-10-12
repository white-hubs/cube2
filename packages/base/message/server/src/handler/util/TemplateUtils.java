package com.guhecloud.cube.base.message2.server.handler.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateUtils {

    /**
     * 定义正则表达式来匹配所有 "${...}" 格式的字符串
     */
    private static final Pattern VARIABLE = Pattern.compile("\\$\\{([^}]+)}");

    private TemplateUtils() {

    }

    public static List<String> getPatternTemplateVariables(String template) {
        List<String> templateContentVariables = new ArrayList<>();
        // 创建 matcher 对象
        Matcher matcher = VARIABLE.matcher(template);
        while (matcher.find()) {
            templateContentVariables.add(matcher.group(1));
        }
        return templateContentVariables;
    }
}
