package com.guhecloud.cube.iot.mgt.core.impl.sql;

import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.List;

/**
 * 设备采样属性历史查询 SQL 构建器
 * 用于查询 *_min_10_any_view 采样视图表
 */
public class SamplingPropQueryBuilder extends ClickhouseSqlBuilder {

    private static final String TIME_COLUMN = "ts";

    // ==================== FROM ====================

    @Override
    public SamplingPropQueryBuilder from(String tableName) {
        super.from(tableName);
        return this;
    }

    // ==================== SELECT 列定义 ====================

    /**
     * 采样数据查询列（带聚合函数）
     *
     * @return 当前构建器实例
     */
    public SamplingPropQueryBuilder selectSamplingColumns() {
        select("groupArray(prop_code) AS propCode, "
                + "FROM_UNIXTIME(ts, '%Y-%m-%d %R:%S') AS date, "
                + "ts, "
                + "groupArray(prop_data_type) AS prop_data_type, "
                + "groupArray(float_value) AS float_value, "
                + "groupArray(int_value) AS int_value, "
                + "groupArray(lcint_value) AS lcint_value, "
                + "groupArray(string_value) AS string_value, "
                + "groupArray(translate_value) AS translate_value");
        return this;
    }

    // ==================== WHERE 条件 ====================

    /**
     * 产品ID条件
     *
     * @param productId 产品ID
     * @return 当前构建器实例
     */
    public SamplingPropQueryBuilder withProductId(String productId) {
        whereEqLiteral("product_id", productId);
        return this;
    }

    /**
     * 设备ID条件
     *
     * @param deviceId 设备ID
     * @return 当前构建器实例
     */
    public SamplingPropQueryBuilder withDeviceId(Long deviceId) {
        and("device_id = '" + deviceId + "'");
        return this;
    }

    /**
     * 属性代码过滤
     *
     * @param propCodes 属性代码列表
     * @return 当前构建器实例
     */
    public SamplingPropQueryBuilder withPropCodes(List<String> propCodes) {
        if (CollectionUtils.isNotEmpty(propCodes)) {
            andIn("prop_code", propCodes);
        }
        return this;
    }

    /**
     * 时间范围条件（使用 DateUtils.parse 转换）
     *
     * @param startTime 开始时间字符串
     * @param endTime   结束时间字符串
     * @return 当前构建器实例
     */
    public SamplingPropQueryBuilder withTimeRange(String startTime, String endTime) {
        if (StringUtils.isNotBlank(startTime)) {
            and(TIME_COLUMN + " >= '" + DateUtils.parse(startTime) + "'");
        }
        if (StringUtils.isNotBlank(endTime)) {
            and(TIME_COLUMN + " <= '" + DateUtils.parse(endTime) + "'");
        }
        return this;
    }

    // ==================== GROUP BY ====================

    /**
     * 按设备ID和时间戳分组
     *
     * @return 当前构建器实例
     */
    public SamplingPropQueryBuilder groupByDeviceAndTs() {
        groupBy("device_id", "ts");
        return this;
    }

    // ==================== 构建结果 ====================

    /**
     * 构建分组计数 SQL（使用 COUNT(DISTINCT ts)）
     *
     * @return 分组计数 SQL 字符串
     */
    public String toDistinctCountSql() {
        return "SELECT COUNT(DISTINCT " + TIME_COLUMN + ")"
                + fromClause.toString()
                + whereClause.toString();
    }
}
