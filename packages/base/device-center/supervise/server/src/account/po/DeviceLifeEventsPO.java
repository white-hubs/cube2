package com.guhecloud.cube.srv.gen.device.supervise.server.account.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 设备台账
 * </p>
 *
 * @author zwb
 * @since 2022-06-26
 */
@Data
@TableName("device_life_events")
public class DeviceLifeEventsPO {

    /**
     * ID
     */
    private Long id;

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
     * 租户编号
     */
    private Integer tenant;

    //流程实例id
    private String processInstanceId;

}
