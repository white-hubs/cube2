package com.guhecloud.cube.base.alarm.server.flow.domain.valueobject;

import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报警状态
 *
 * @author lanchun
 * @date 2023/2/28 11:38
 */
@Getter
@AllArgsConstructor
public class AlarmStatus {

    /**
     * 报警状态
     */
    private AlarmStatusEnum alarmStatus;

    /**
     * 报警状态序号
     */
    private Integer alarmStatusOrder;
}
