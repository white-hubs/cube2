package com.guhecloud.cube.base.alarm.server.flow.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 报警确认信息
 *
 * @author lanchun
 * @date 2023/2/28 11:43
 */
@Getter
@AllArgsConstructor
public class AlarmConfirmed {

    /**
     * 报警确认人员
     */
    private Long alarmConfirmedBy;

    /**
     * 报警确认人员名称
     */
    private String alarmConfirmedName;

    /**
     * 报警确认时间
     */
    private LocalDateTime alarmConfirmedTime;
}
