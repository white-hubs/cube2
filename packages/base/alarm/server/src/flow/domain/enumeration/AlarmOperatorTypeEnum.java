package com.guhecloud.cube.base.alarm.server.flow.domain.enumeration;

import lombok.NonNull;

/**
 * 报警操作日志类型枚举
 *
 * @author lanchun
 * @date 2022/6/14 15:17
 */
public enum AlarmOperatorTypeEnum {
    ALARM_OCCURRENCE("报警发生"),
    ALARM_CONFIRMED("报警确认"),
    ALARM_LINKAGE_DEVICE("联动设备"),
    ALARM_SUSPEND("挂起"),
    ALARM_TRANSFER("移交"),
    ALARM_ASSIGN("报警指派"),
    ALARM_HANDLER("报警处理"),
    ALARM_FOLLOW_UP("报警跟进记录"),
    ALARM_URGING("报警催办"),
    ALARM_MESSAGE_NOTIFY("消息通知"),
    ALARM_AI_ANALYSIS_RESULT("AI辅助分析结果");
    /**
     * 描述信息
     */
    private final String description;

    AlarmOperatorTypeEnum(@NonNull String description) {
        this.description = description;
    }
}
