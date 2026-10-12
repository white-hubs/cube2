package com.guhecloud.cube.iot.mgt.core.data.vo.linkage;

import lombok.Data;

@Data
public class RuleTriggerSnapshot {
    private String deviceId;
    private String propCode;
    private String propValue;
    private String propValueTs;
    private String condition;
}
