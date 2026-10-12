package com.guhecloud.cube.biz.linkage.server.facade.iot.reqeust;

import lombok.Data;

@Data
public class PropertyControlRequest {

    private String commandSeq;
    private String deviceId;
    private String deviceName;
    private String propCode;
    private String propValue;
}
