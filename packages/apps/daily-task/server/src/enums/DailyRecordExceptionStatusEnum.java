package com.guhecloud.cube.biz.daily.task.enums;

import lombok.Getter;

@Getter
public enum DailyRecordExceptionStatusEnum {
    WAIT_REPAIR("待修复"),
    REPAIRED("已修复");

    private final String value;

    DailyRecordExceptionStatusEnum(String value) {
        this.value = value;
    }
}
