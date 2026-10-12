package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.vo;

import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration.CompareOpEnum;
import lombok.Data;

@Data
public class DeviceNotifyRuleConditionExpVO {
    private Long id;
    private String propCode;
    private String propName;
    private String propUnit;
    private CompareOpEnum op;
    private String oprand;
    private Integer delayTime;
    private Integer startDelayTime;
    private Integer endDelayTime;
}