package com.guhecloud.cube.iot.north.bridge.client.pj.param;

import lombok.Data;

@Data
public class DeviceDetailParam {
    private String  deviceId;
    private boolean withSnapShot;
    private boolean withEventSpec;
    private boolean withCommandSpec;
    private boolean withPropertySpec;
}
