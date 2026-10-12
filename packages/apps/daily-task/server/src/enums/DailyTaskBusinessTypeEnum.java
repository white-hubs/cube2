package com.guhecloud.cube.biz.daily.task.enums;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/31 14:22
 * @Description:
 */
@Getter
public enum DailyTaskBusinessTypeEnum {

    DAILY_TASK("日常任务"),
    DAILY_INSPECTION("日常巡检"),
    DAILY_SMALL_INSPECTION("小散工程"),
    GRID_MANAGE("网格管理");
    private final String applicationName;

    DailyTaskBusinessTypeEnum(String applicationName) {
        this.applicationName = applicationName;
    }
}
