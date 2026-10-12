package com.guhecloud.cube.biz.subsystem.broadcast.server.common.infrastruceture.facade.iot.request;

import lombok.Data;

@Data
public class GetDeviceAttributeRunningRequest {

    private String deviceId;
    private boolean withSnapShot;
    private boolean withEventSpec;
    private boolean withCommandSpec;
    private boolean withPropertySpec;
}
