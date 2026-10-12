package com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command;

import lombok.Data;

import java.util.Set;

@Data
public class ModifyTrafficDetectionAreaCommand {

    /**
     * 设备id集合
     */
    private Set<Long> deviceIds;
}
