package com.guhecloud.cube.base.alarm.server.flow.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 报警信息
 *
 * @author lanchun
 * @date 2023/2/28 11:24
 */
@Getter
@AllArgsConstructor
public class AlarmInfo {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 报警事件
     */
    private String alarmEvent;

    /**
     * 报警事件名称
     */
    private String alarmEventName;

    /**
     * 报警阀值
     */
    private String alarmThreshold;

    /**
     * 报警发生时间
     */
    private LocalDateTime alarmTime;
}
