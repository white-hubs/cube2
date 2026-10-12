package com.guhecloud.cube.biz.daily.task.enums;

import lombok.Getter;

/**
 * 执行记录聚合状态
 */
@Getter
public enum DailyRecordStatusEnum {
    NORMAL("正常"),
    ABNORMAL_WAIT_REPAIR("异常待修复"),
    ABNORMAL_REPAIRING("异常修复中"),
    ABNORMAL_REPAIRED("异常已修复");

    private final String value;

    DailyRecordStatusEnum(String value) {
        this.value = value;
    }
}
