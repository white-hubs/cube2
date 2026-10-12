package com.guhecloud.cube.iot.mgt.core.data.north.condition;

import lombok.Data;

@Data
public class DeviceCommandCondition implements SubscribeCondition {
    private String commandId;
}
