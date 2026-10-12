package com.guhecloud.cube.iot.mgt.core.data.north.condition;

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