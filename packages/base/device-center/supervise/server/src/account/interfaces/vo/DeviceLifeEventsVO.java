package com.guhecloud.cube.srv.gen.device.supervise.server.account.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceLifeEventsVO {

    /**
     * 设备ID
     */
    private Long deviceId;

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
     * 事件类型
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
     * 跳转地址
     */
    private String url;

    //流程实例id
    private String processInstanceId;

}
