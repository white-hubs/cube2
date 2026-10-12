package com.guhecloud.cube.iot.mgt.device.vo;

import lombok.Data;

@Data
public class DeviceCreateEvent {
    private Long deviceId;
    private String deviceName;
    private String deviceCode;
    private Long productId;
}
