package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DeviceEventHistoryDTO {
    private Long deviceId;
    private String eventCode;
    private String instanceId;
    private String category;
    private String bizType;
    private Date eventTime;
}
