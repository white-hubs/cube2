package com.guhecloud.cube.base.device.center.open.vo;

import lombok.Data;

import java.util.List;

@Data
public class DeviceTags {
    private String deviceId;
    private List<DeviceTag> tags;

}
