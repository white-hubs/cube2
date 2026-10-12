package com.guhecloud.cube.base.alarm.server.flow.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报警事件类型
 *
 * @author lanchun
 * @date 2023/9/12 13:43
 */
@Getter
@AllArgsConstructor
public class AlarmEventType {

    /**
     * 报警事件分类主键
     */
    private Long alarmEventTypeId;

    /**
     * 报警事件分类编号
     */
    private String alarmEventTypeCode;

    /**
     * 报警事件分类名称
     */
    private String alarmEventTypeName;
}
