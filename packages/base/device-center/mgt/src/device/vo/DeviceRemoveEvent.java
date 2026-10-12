package com.guhecloud.cube.iot.mgt.device.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DeviceRemoveEvent {
    /**
     * 移除设备列表
     */
    private List<DeviceRemoveInfo> devices;

}
