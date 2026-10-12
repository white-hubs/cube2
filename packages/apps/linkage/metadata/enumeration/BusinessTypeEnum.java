package com.guhecloud.apps.linkage.metadata.enumeration;

/**
 * 业务类型
 *
 * @author lanchun
 * @date 2022/6/21 10:13
 */
public enum BusinessTypeEnum {
    EMERGENCY_CENTER_ALARM("预案报警"),
    DEVICE_ALARM("设备报警"),
    DEVICE_HEALTH_ALARM("设备健康报警"),
    DEVICE_CONDITION("设备条件"),
    ENERGY_ALARM("能耗报警"),
    LINE_LOSS_ALARM("线损报警"),
    TIMING_CONTROL("定时控制"),
    SCENE_CONTROL("场景控制");

    /**
     * 描述信息
     */
    private final String description;

    BusinessTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
