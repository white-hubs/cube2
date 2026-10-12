package com.guhecloud.cube.base.device.center.metadata.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceGroupChange {

    public static final String TOPIC = "DEVICE_GROUP_CHANGE";

    /**
     * 设备组id
     */
    private Long id;
}
