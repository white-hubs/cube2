package com.guhecloud.cube.srv.gen.device.supervise.client.enumeration;

import lombok.Getter;

@Getter
public enum DeviceLifeEventTypeEnum {

    /**
     * 日常巡检
     */
    INSPECTION("日常巡检"),

    /**
     * 维保作业
     */
    UPKEEP("维保作业"),

    /**
     * 故障报修
     */
    FAULT("故障报修"),

    /**
     * 更新改造
     */
    RETROFIT("更新改造"),

    /**
     * 隐患排查
     */
    HIDDEN_DANGER("隐患排查"),

    OUTSOURCE_REPAIR("委外维修");

    /**
     * 描述信息
     */
    private final String description;

    DeviceLifeEventTypeEnum(String description) {
        this.description = description;
    }
}
