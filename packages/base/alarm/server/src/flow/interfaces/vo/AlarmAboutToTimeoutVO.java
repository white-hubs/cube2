package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class AlarmAboutToTimeoutVO {

    private Long id;

    /**
     * 报警对象名称
     */
    private String alarmTargetName;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 报警时间名称
     */
    private String alarmEventName;

    /**
     * 报警时间
     */
    private LocalDateTime alarmTime;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 用户主键集合
     */
    private Set<Long> userIds;
}
