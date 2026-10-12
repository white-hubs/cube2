package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.valueobject;

import lombok.Data;

import java.util.Map;

@Data
public class EventSnapshot {
    private String deviceId;
    private String eventCode;
    private String instanceId;
    private String timestamp;
    private String delayEventTime;
    private Map<String, Object> relatedProp;
    private String notifyStatus;
    private String notifyStatusTs;
}
