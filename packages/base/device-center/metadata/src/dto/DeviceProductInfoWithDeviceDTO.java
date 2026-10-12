package com.guhecloud.cube.base.device.center.metadata.dto;

import lombok.Data;

@Data
/**
 * @author pengjian
 * @date 2024/1/19 10:11
 * @description
 */
public class DeviceProductInfoWithDeviceDTO {
    private Long deviceId;

    private DeviceProductInfoDTO productInfo;
}
