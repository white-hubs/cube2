package com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command;

import lombok.Data;

@Data
public class ModifyTrafficDetectionAreaDeviceCommand {

    private Long id;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 设备配置
     */
    private String deviceConfig;
}
