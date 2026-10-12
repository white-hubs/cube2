package com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceLifeEventMessage {

    public final static String TOPIC = "DEVICE_LIFE_EVENT";

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 应用编码
     */
    private String applicationCode;

    /**
     * 应用名
     */
    private String applicationName;

    /**
     * 事件ID
     */
    private Long eventId;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件类型名
     */
    private String eventTypeName;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件状态
     */
    private String eventStatus;

    /**
     * 事件时间
     */
    private LocalDateTime eventTime;

    /**
     * 报警快照
     */
    private String alarmSnapshot;

    /**
     * 设备ID
     */
    private Set<Long> deviceIds;
}
