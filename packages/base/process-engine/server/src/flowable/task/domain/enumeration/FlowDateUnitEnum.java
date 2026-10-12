package com.guhecloud.cube.base.flow.flowable.task.domain.enumeration;

/**
 * @Author: lijie
 * @CreateTime: 2024-05-21  17:29
 * @Description:
 * @Version: 1.0
 */
public enum FlowDateUnitEnum {
    MINUTE("分"),
    HOUR("小时"),
    DAY("天");
    /**
     * 描述信息
     */
    private final String description;

    FlowDateUnitEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static FlowDateUnitEnum getByName(String name) {
        for (FlowDateUnitEnum flowDateUnitEnum : values()) {
            if (flowDateUnitEnum.name().equals(name)) {
                return flowDateUnitEnum;
            }
        }
        return null;
    }
}
