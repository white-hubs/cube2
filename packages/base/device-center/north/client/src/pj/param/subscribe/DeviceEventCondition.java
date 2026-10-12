package com.guhecloud.cube.iot.north.bridge.client.pj.param.subscribe;

import lombok.Data;

import java.util.List;

@Data
public class DeviceEventCondition implements SubscribeCondition {

    /**
     * deviceId.propCode<br/>
     * deviceId.eventCode
     */
    private List<String> deviceIdAndEventCode;
}