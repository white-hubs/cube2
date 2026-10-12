package com.guhecloud.cube.biz.daily.task.enums;

import lombok.Getter;

/**
 * 执行记录故障单类型枚举
 */
@Getter
public enum DailyWorkOrderTypeEnum {
    HIDDENDANGER("隐患排查"),
    FAULT("故障报修");
    private final String value;

    DailyWorkOrderTypeEnum(String value) {
        this.value = value;
    }
}
