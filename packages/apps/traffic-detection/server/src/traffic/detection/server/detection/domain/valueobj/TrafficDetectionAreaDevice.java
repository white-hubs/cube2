package com.guhecloud.cube.biz.traffic.detection.server.detection.domain.valueobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TrafficDetectionAreaDevice {

    private Long deviceId;

    /**
     * 设备配置
     */
    @Setter
    private String deviceConfig;

    public TrafficDetectionAreaDevice(Long deviceId) {
        this.deviceId = deviceId;
    }
}
