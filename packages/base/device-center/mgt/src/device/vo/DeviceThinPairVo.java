package com.guhecloud.cube.iot.mgt.device.vo;

import lombok.Data;

@Data
public class DeviceThinPairVo {
    private Long deviceId;
    private Long deviceParentId;
    private String deviceName;
    private Long productId;
    private String deviceNumber;
}
