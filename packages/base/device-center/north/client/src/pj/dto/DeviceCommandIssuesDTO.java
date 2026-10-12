package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

@Data
public class DeviceCommandIssuesDTO {
    private Long deviceId;
    private String eventCode;
    private String thresholdCode;//阈值code
    private String value;
    private String commandSeq;
}
