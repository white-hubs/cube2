package com.guhecloud.cube.iot.mgt.core.impl.sql;

import com.guhecloud.platform.base.util.StringUtils;

import java.util.Objects;

/**
 * 联动规则触发日志查询 SQL 构建器
 */
public class LinkageQueryBuilder extends ClickhouseSqlBuilder {

    private static final String TIME_COLUMN = "insert_time";

    // ==================== FROM ====================

    @Override
    public LinkageQueryBuilder from(String tableName) {
        super.from(tableName);
        return this;
    }

    // ==================== SELECT 列定义 ====================

    /**
     * 联动日志查询列 (全部字段)
     *
     * @return 当前构建器实例
     */
    public LinkageQueryBuilder selectAll() {
        select("*");
        return this;
    }

    // ==================== WHERE 条件 ====================

    /**
     * 规则ID条件
     *
     * @param ruleId 规则ID
     * @return 当前构建器实例
     */
    public LinkageQueryBuilder withRuleId(String ruleId) {
        if (StringUtils.isNotBlank(ruleId)) {
            if (!hasWhereCondition) {
                whereEqLiteral("ruleId", ruleId);
            } else {
                and("ruleId = '" + ruleId + "'");
            }
        }
        return this;
    }

    /**
     * 实例ID条件
     *
     * @param instanceId 实例ID
     * @return 当前构建器实例
     */
    public LinkageQueryBuilder withInstanceId(String instanceId) {
        if (StringUtils.isNotBlank(instanceId)) {
            if (!hasWhereCondition) {
                whereEqLiteral("instanceId", instanceId);
            } else {
                and("instanceId = '" + instanceId + "'");
            }
        }
        return this;
    }

    /**
     * 租户ID条件
     *
     * @param tenantId 租户ID
     * @return 当前构建器实例
     */
    public LinkageQueryBuilder withTenantId(Integer tenantId) {
        if (Objects.nonNull(tenantId)) {
            if (!hasWhereCondition) {
                whereEqLiteral("tenantId", tenantId);
            } else {
                and("tenantId = " + tenantId);
            }
        }
        return this;
    }

    /**
     * 时间范围条件
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 当前构建器实例
     */
    public LinkageQueryBuilder withTimeRange(String startTime, String endTime) {
        andTimeRangeLiteral(TIME_COLUMN, startTime, endTime);
        return this;
    }
}
