package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficDetectionAreaDeviceVO {

    private Long deviceId;

    /**
     * 设备配置
     */
    private String deviceConfig;

}
