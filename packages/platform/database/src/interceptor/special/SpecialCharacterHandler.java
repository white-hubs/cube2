package com.guhecloud.platform.database.interceptor.special;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.reflection.MetaObject;

public interface SpecialCharacterHandler {

    void transferLikeSql(String sql, MetaObject metaObject, Object parameterObject, BoundSql boundSql);
}
