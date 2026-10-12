package com.guhecloud.cube.base.device.center.core.rule.tag.enumeration;

import lombok.Getter;

/**
 * 设备支持的条件字段
 *
 * @author lanchun
 * @date 2025/4/9 19:37
 */
@Getter
public enum DeviceConditionFieldEnum {

    /**
     * 根据产品过滤条件
     */
    PRODUCT("device_iot_prod_id", "deviceIotProdId"),

    /**
     * 根据区域过滤条件
     */
    AREA("area_last_id", "areaId"),

    /**
     * 根据区域过滤条件
     */
    DEVICE("id", "id"),

    /**
     * 根据设备名称过滤条件
     */
    DEVICE_NAME("device_name", "deviceName");

    private final String tableColumn;

    private final String modelColumn;

    DeviceConditionFieldEnum(String tableColumn, String modelColumn) {
        this.tableColumn = tableColumn;
        this.modelColumn = modelColumn;
    }

    public static DeviceConditionFieldEnum getByModelColumn(String modelColumn) {
        for (DeviceConditionFieldEnum deviceConditionFieldEnum : DeviceConditionFieldEnum.values()) {
            if (modelColumn.equals(deviceConditionFieldEnum.getModelColumn())) {
                return deviceConditionFieldEnum;
            }
        }
        return null;
    }
}
