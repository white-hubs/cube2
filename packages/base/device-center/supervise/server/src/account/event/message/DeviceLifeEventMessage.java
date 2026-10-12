package com.guhecloud.cube.srv.gen.device.supervise.server.account.event.message;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
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
     * 任务删除状态
     */
    private Boolean delState;

    /**
     * 设备ID
     */
    private Set<Long> deviceIds;

    //流程实例id
    private String processInstanceId;

}
