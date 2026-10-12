package com.guhecloud.platform.database.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.guhecloud.platform.database.config.MybatisPlusConfigurer;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

/**
 * 借助租户隔离功能实现删除数据排除拦截，数据查询自动过滤已删除数据
 *
 * @author Woody
 * @date 2021/7/6
 * @see MybatisPlusConfigurer
 */
public class DeletedExcludeHandler implements TenantLineHandler {
    /**
     * 删除标记字段名称
     */
    public static final String DELETED_COLUMN_NAME = "deleted";

    public DeletedExcludeHandler() {

    }

    @Override
    public Expression getTenantId() {
        return new LongValue(0);
    }

    @Override
    public String getTenantIdColumn() {
        return DELETED_COLUMN_NAME;
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return !DatabaseContextHolder.isDeletableTable(tableName);
    }
}
