package com.guhecloud.cube.biz.daily.task.enums;

import lombok.Getter;

/**
 * 计划状态枚举
 */
@Getter
public enum DailyPlanStatusEnum {
    RUNNING("执行中"),
    PAUSED("暂停中"),
    FINISHED("已完成"), // 过了有效期,表示正常完成使命
    ;
    private final String value;

    DailyPlanStatusEnum(String value) {
        this.value = value;
    }
}
