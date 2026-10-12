package com.guhecloud.platform.database.interceptor.special;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.reflection.MetaObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class DmSpecialCharacterHandler extends DefaultSpecialCharacterHandler {

    private static final Pattern pattern = Pattern.compile("(?i)LIKE\\s+((?:'[^']*')|(?:CONCAT\\([^\\)]*\\))|\\?)");

    @Override
    public void transferLikeSql(String sql, MetaObject metaObject, Object parameterObject, BoundSql boundSql) {
        super.transferLikeSql(sql, metaObject, parameterObject, boundSql);
        Matcher matcher = pattern.matcher(sql);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, matcher.group() + " ESCAPE '\\\\'");
        }
        matcher.appendTail(result);
        metaObject.setValue("delegate.boundSql.sql", result.toString());
    }
}
