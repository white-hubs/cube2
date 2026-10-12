package com.guhecloud.cube.iot.north.bridge.client.pj.param.subscribe;

import lombok.Data;

import java.util.List;

@Data
public class DeviceStatusCondition implements SubscribeCondition {

    private List<String> deviceIds;
}