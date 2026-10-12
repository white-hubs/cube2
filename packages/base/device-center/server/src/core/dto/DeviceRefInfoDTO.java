package com.guhecloud.cube.base.device.center.core.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeviceRefInfoDTO {
    private Long deviceId;
    private List<String> refDeviceIds;
}
