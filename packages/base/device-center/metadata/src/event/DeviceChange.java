package com.guhecloud.cube.base.device.center.metadata.event;

import com.guhecloud.cube.base.device.center.metadata.lang.DeviceChangeEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import lombok.Data;

import java.util.List;

/**
 * 设备事件<br/>
 * 设备中心对设备任意更改都将发送事件
 */
@Data
public class DeviceChange {
    private List<Pair<DeviceInfo, DeviceChangeEnum>> deviceChangePairs;
    private long ts;
}
