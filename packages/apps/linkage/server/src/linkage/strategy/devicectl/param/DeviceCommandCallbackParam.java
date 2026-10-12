package com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.param;

import lombok.Data;

@Data
public class DeviceCommandCallbackParam {

    private String commandSeq;

    private String executeResult;

    private String message;
    /**
     * 设备id
     */
    private Long deviceInstanceId;
}
