package com.guhecloud.cube.base.device.center.metadata.event;

import lombok.Data;

@Data
public class DeviceInfo {
    /**
     * 设备ID
     */
    private Long deviceId;
    /**
     * 设备产品ID
     */
    private Long deviceProductId;

    public static DeviceInfo of(Long deviceId, Long deviceProductId) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceId(deviceId);
        deviceInfo.setDeviceProductId(deviceProductId);
        return deviceInfo;
    }
}
