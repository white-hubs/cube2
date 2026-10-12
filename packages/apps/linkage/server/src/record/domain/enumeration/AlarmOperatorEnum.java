package com.guhecloud.cube.biz.linkage.server.record.domain.enumeration;

import lombok.Getter;

/**
 * 报警操作枚举
 *
 * @author lanchun
 * @date 2023/5/30 9:28
 */
@Getter
public enum AlarmOperatorEnum {

    /**
     * 报警确认
     */
    ALARM_CONFIRMED,

    /**
     * 报警挂起
     */
    ALARM_SUSPEND,

    /**
     * 报警移交
     */
    ALARM_TRANSFER,

    /**
     * 报警指派
     */
    ALARM_ASSIGN,

    /**
     * 报警处理
     */
    ALARM_HANDLER,
}
