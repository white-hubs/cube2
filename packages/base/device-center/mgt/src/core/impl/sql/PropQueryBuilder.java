package com.guhecloud.cube.iot.mgt.core.impl.sql;

import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.MultiDevicePropHistoryQueryVo;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

/**
 * 设备属性历史查询 SQL 构建器
 */
public class PropQueryBuilder extends ClickhouseSqlBuilder {

    private static final String TIME_COLUMN = "source_update_time";

    // ==================== FROM ====================

    @Override
    public PropQueryBuilder from(String tableName) {
        super.from(tableName);
        return this;
    }

    // ==================== SELECT 列定义 ====================

    /**
     * 原始数据查询列 (单设备)
     *
     * @return 当前构建器实例
     */
    public PropQueryBuilder selectRawColumns() {
        select("prop_code, "
                + "prop_data_type, "
                + "source_update_time AS ts, "
                + "float_value, "
                + "string_value, "
                + "int_value, "
                + "lcint_value, "
                + "translate_value");
        return this;
    }

    /**
     * 原始数据查询列 (多设备，含 device_id)
     *
     * @return 当前构建器实例
     */
    public PropQueryBuilder selectRawColumnsWithDevice() {
        select("device_id, "
                + "prop_code, "
                + "prop_data_type, "
                + "source_update_time AS ts, "
                + "float_value, "
                + "string_value, "
                + "int_value, "
                + "lcint_value, "
                + "translate_value");
        return this;
    }

    /**
     * 采样聚合查询列
     *
     * @param aggType 聚合类型 (min, max, avg, sum, any)
     * @param step    时间步长 (分钟)
     * @return 当前构建器实例
     */
    public PropQueryBuilder selectSamplingColumns(String aggType, int step) {
        select("device_id, "
                + "any(device_name) AS device_name, "
                + "prop_code, "
                + "any(prop_name) AS prop_name, "
                + "any(prop_data_type) AS prop_data_type, "
                + "toStartOfInterval(source_update_time, INTERVAL " + step + " MINUTE) AS ts, "
                + aggType + "(float_value) AS float_value, "
                + "any(string_value) AS string_value, "
                + "CAST(" + aggType + "(int_value) AS Int32) AS int_value, "
                + "any(lcint_value) AS lcint_value");
        return this;
    }

    // ==================== WHERE 条件 ====================

    /**
     * 设备ID条件
     *
     * @param deviceId 设备ID
     * @return 当前构建器实例
     */
    public PropQueryBuilder withDeviceId(Object deviceId) {
        whereEqLiteral("device_id", String.valueOf(deviceId));
        return this;
    }

    /**
     * 产品ID条件
     *
     * @param productId 产品ID
     * @return 当前构建器实例
     */
    public PropQueryBuilder withProductId(String productId) {
        and("product_id = '" + productId + "'");
        return this;
    }

    /**
     * 时间范围条件
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 当前构建器实例
     */
    public PropQueryBuilder withTimeRange(String startTime, String endTime) {
        andTimeRange(TIME_COLUMN, startTime, endTime);
        return this;
    }

    /**
     * 属性代码过滤
     *
     * @param propCodes 属性代码列表
     * @return 当前构建器实例
     */
    public PropQueryBuilder withPropCodes(List<String> propCodes) {
        andIn("prop_code", propCodes);
        return this;
    }

    /**
     * 设备-属性元组条件 (多设备查询)
     *
     * @param items            查询项列表
     * @param includeProductId 是否包含 productId
     * @return 当前构建器实例
     */
    public PropQueryBuilder withDevicePropTuples(List<MultiDevicePropHistoryQueryVo.QueryItem> items, boolean includeProductId) {

        if (CollectionUtils.isEmpty(items)) {
            return this;
        }

        StringBuilder tupleCondition = new StringBuilder();
        if (includeProductId) {
            tupleCondition.append("(device_id, prop_code, product_id) IN (");
            for (int i = 0; i < items.size(); i++) {
                if (i > 0) {
                    tupleCondition.append(",");
                }
                MultiDevicePropHistoryQueryVo.QueryItem item = items.get(i);
                tupleCondition.append("('").append(item.getDeviceId())
                        .append("','").append(item.getPropCode())
                        .append("','").append(item.getProductId()).append("')");
            }
        } else {
            tupleCondition.append("(device_id, prop_code) IN (");
            for (int i = 0; i < items.size(); i++) {
                if (i > 0) {
                    tupleCondition.append(",");
                }
                MultiDevicePropHistoryQueryVo.QueryItem item = items.get(i);
                tupleCondition.append("('").append(item.getDeviceId())
                        .append("','").append(item.getPropCode()).append("')");
            }
        }
        tupleCondition.append(")");
        and(tupleCondition.toString());
        return this;
    }

    // ==================== GROUP BY ====================

    /**
     * 采样分组
     *
     * @param step 时间步长 (分钟)
     * @return 当前构建器实例
     */
    public PropQueryBuilder groupBySampling(int step) {
        groupBy("device_id", "prop_code", "toStartOfInterval(source_update_time, INTERVAL " + step + " MINUTE)");
        return this;
    }
}
