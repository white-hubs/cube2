package com.guhecloud.cube.iot.mgt.core.data.north.condition;

import lombok.Data;

import java.util.List;

@Data
public class DeviceStatusCondition implements SubscribeCondition {

    private List<String> deviceIds;
}