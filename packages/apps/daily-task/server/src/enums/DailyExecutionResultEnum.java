package com.guhecloud.cube.biz.daily.task.enums;

import lombok.Getter;

/**
 * 执行结果
 */
@Getter
public enum DailyExecutionResultEnum {
    NORMAL("正常"),
    ABNORMAL("异常"),
    ;
    private final String value;

    DailyExecutionResultEnum(String value) {
        this.value = value;
    }
}
