package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

@Data
public class RuleTriggerSnapshotDTO {
    private String deviceId;
    private String propCode;
    private String propValue;
    private String propValueTs;
    private String condition;
}
