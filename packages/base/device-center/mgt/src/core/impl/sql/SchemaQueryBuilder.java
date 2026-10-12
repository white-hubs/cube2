package com.guhecloud.cube.iot.mgt.core.impl.sql;

import com.guhecloud.platform.base.util.StringUtils;

/**
 * 设备 Schema 信息查询 SQL 构建器
 */
public class SchemaQueryBuilder extends ClickhouseSqlBuilder {

    private static final String TIME_COLUMN = "insert_time";

    // ==================== FROM ====================

    @Override
    public SchemaQueryBuilder from(String tableName) {
        super.from(tableName);
        return this;
    }

    @Override
    public SchemaQueryBuilder from(String tableName, boolean useFinal) {
        super.from(tableName, useFinal);
        return this;
    }

    // ==================== SELECT 列定义 ====================

    /**
     * Schema 查询列
     *
     * @return 当前构建器实例
     */
    public SchemaQueryBuilder selectSchemaColumns() {
        select("tenantId, "
                + "devNum, "
                + "devName, "
                + "insert_time, "
                + "isRoot AS isRoot, "
                + "parentDevNum AS parentDevNum, "
                + "parentDevName AS parentDevName, "
                + "schema AS schema");
        return this;
    }

    // ==================== WHERE 条件 ====================

    /**
     * 时间范围条件
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 当前构建器实例
     */
    public SchemaQueryBuilder withTimeRange(String startTime, String endTime) {
        andTimeRangeLiteral(TIME_COLUMN, startTime, endTime);
        return this;
    }

    /**
     * 租户ID条件
     *
     * @param tenantId 租户ID
     * @return 当前构建器实例
     */
    public SchemaQueryBuilder withTenantId(String tenantId) {
        if (StringUtils.isNotBlank(tenantId)) {
            and("tenantId = " + tenantId);
        }
        return this;
    }

    /**
     * 设备编号模糊查询
     *
     * @param devNumLike 设备编号
     * @return 当前构建器实例
     */
    public SchemaQueryBuilder withDevNumLike(String devNumLike) {
        andLike("devNum", devNumLike);
        return this;
    }

    /**
     * 设备名称模糊查询
     *
     * @param devNameLike 设备名称
     * @return 当前构建器实例
     */
    public SchemaQueryBuilder withDevNameLike(String devNameLike) {
        andLike("devName", devNameLike);
        return this;
    }

    /**
     * 父设备编号条件
     *
     * @param parentDevNum 父设备编号
     * @return 当前构建器实例
     */
    public SchemaQueryBuilder withParentDevNum(String parentDevNum) {
        if (StringUtils.isNotBlank(parentDevNum)) {
            and("parentDevNum = '" + parentDevNum + "'");
        }
        return this;
    }

    /**
     * 仅查询根设备
     *
     * @param onlyParent 是否仅查询根设备
     * @return 当前构建器实例
     */
    public SchemaQueryBuilder withOnlyParent(boolean onlyParent) {
        if (onlyParent) {
            and("isRoot = 1");
        }
        return this;
    }
}
