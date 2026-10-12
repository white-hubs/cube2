package com.guhecloud.cube.biz.linkage.server.facade.iot.reqeust;

import lombok.Data;

@Data
public class RuleRequest {
    private String deviceId;
    private String op;
    private Integer delayTime;
}
