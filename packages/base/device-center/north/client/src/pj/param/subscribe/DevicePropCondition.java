package com.guhecloud.cube.iot.north.bridge.client.pj.param.subscribe;

import lombok.Data;

import java.util.List;

@Data
public class DevicePropCondition implements SubscribeCondition {

    /**
     * deviceId.propCode<br/>
     * deviceId.eventCode
     */
    private List<String> deviceIdAndPropCode;

    private boolean changeNotify;
}
