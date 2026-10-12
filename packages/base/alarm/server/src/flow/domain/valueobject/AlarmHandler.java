package com.guhecloud.cube.base.alarm.server.flow.domain.valueobject;

import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmHandlerResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 报警处理
 *
 * @author lanchun
 * @date 2023/2/28 11:46
 */
@Getter
@AllArgsConstructor
public class AlarmHandler {

    /**
     * 报警处理人员
     */
    private String alarmHandlerBy;

    /**
     * 报警处理人员名称
     */
    private String alarmHandlerName;

    /**
     * 报警处理时间
     */
    private LocalDateTime alarmHandlerTime;

    /**
     * 报警处理结果
     */
    private AlarmHandlerResultEnum alarmHandlerResult;

    /**
     * 报警处理原因
     */
    private AlarmReason alarmHandlerReason;

    /**
     * 报警处理结果描述
     */
    private String alarmHandlerResultRemark;

    /**
     * 处理耗时
     */
    private long processingTime;
}
