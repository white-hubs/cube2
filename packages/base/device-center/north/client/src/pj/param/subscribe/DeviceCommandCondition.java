package com.guhecloud.cube.iot.north.bridge.client.pj.param.subscribe;

import lombok.Data;

@Data
public class DeviceCommandCondition implements SubscribeCondition {
    private String commandId;
}
