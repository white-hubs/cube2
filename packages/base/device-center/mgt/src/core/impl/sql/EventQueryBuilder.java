package com.guhecloud.cube.iot.mgt.core.impl.sql;

import com.guhecloud.platform.base.util.StringUtils;

import java.util.List;

/**
 * 设备事件历史查询 SQL 构建器
 */
public class EventQueryBuilder extends ClickhouseSqlBuilder {

    private static final String TIME_COLUMN = "eventTime";

    // ==================== FROM ====================

    @Override
    public EventQueryBuilder from(String tableName) {
        super.from(tableName);
        return this;
    }

    // ==================== SELECT 列定义 ====================

    /**
     * 事件查询列
     *
     * @return 当前构建器实例
     */
    public EventQueryBuilder selectEventColumns() {
        select("deviceId, "
                + "eventCode, "
                + "deviceName, "
                + "instanceId, "
                + "eventName, "
                + "bizType, "
                + "category, "
                + "eventTime");
        return this;
    }

    // ==================== WHERE 条件 ====================

    /**
     * 设备ID条件
     *
     * @param deviceId 设备ID
     * @return 当前构建器实例
     */
    public EventQueryBuilder withDeviceId(Object deviceId) {
        whereEqLiteral("deviceId", String.valueOf(deviceId));
        return this;
    }

    /**
     * 业务类型条件
     *
     * @param bizType 业务类型
     * @return 当前构建器实例
     */
    public EventQueryBuilder withBizType(String bizType) {
        if (StringUtils.isNotBlank(bizType)) {
            and("bizType = '" + bizType + "'");
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
    public EventQueryBuilder withTimeRange(String startTime, String endTime) {
        andTimeRange(TIME_COLUMN, startTime, endTime);
        return this;
    }

    /**
     * 事件代码过滤
     *
     * @param eventCodes 事件代码列表
     * @return 当前构建器实例
     */
    public EventQueryBuilder withEventCodes(List<String> eventCodes) {
        andIn("eventCode", eventCodes);
        return this;
    }
}
