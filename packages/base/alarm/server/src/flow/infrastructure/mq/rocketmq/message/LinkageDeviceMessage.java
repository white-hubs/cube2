package com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.message;

import lombok.Data;

/**
 * 联动设备信息
 *
 * @author lanchun
 * @date 2022/11/1 16:44
 */
@Data
public class LinkageDeviceMessage {

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

}
