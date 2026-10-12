package com.guhecloud.cube.base.alarm.server.flow.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AlarmSuspend {

    /**
     * 挂起时间
     */
    private LocalDateTime suspendTime;

    /**
     * 预计处理时间
     */
    private LocalDateTime estimateHandlerTime;
}
