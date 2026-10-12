package com.guhecloud.cube.base.device.center.scene.rocketmq.message;

import com.guhecloud.cube.base.device.center.metadata.event.DeviceInfo;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.base.device.center.metadata.lang.DeviceChangeEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import lombok.Data;

import java.util.List;

@Data
public class DeviceChangeEventMessage {

    public static final String TOPIC = Const.DEVICE_CHANGE_TOPIC;

    private List<Pair<DeviceInfo, DeviceChangeEnum>> deviceChangePairs;

    private long ts;
}
