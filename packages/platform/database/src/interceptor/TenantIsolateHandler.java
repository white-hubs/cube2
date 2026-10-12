package com.guhecloud.platform.database.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.database.config.MybatisPlusConfigurer;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

/**
 * 数据隔离拦截器，数据查询自动加入租户筛选逻辑
 *
 * @author Woody
 * @date 2021/7/6
 * @see MybatisPlusConfigurer
 */
public class TenantIsolateHandler implements TenantLineHandler {
    /**
     * 租户编号字段名称
     */
    public static final String TENANT_COLUMN_NAME = "tenant";

    public TenantIsolateHandler() {
    }

    @Override
    public Expression getTenantId() {
        return new LongValue(SessionContextHolder.getRequester().getTenant());
    }

    @Override
    public String getTenantIdColumn() {
        return TENANT_COLUMN_NAME;
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return !DatabaseContextHolder.isIsolatableTable(tableName)
               || SessionContextHolder.getRequesterProperty(Requester::getTenant) == null;
    }
}
