package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.valueobj;

import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration.CompareOpEnum;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class DeviceNotifyRuleConditionExp {
    private Long conditionId;
    private String propCode;
    private String propName;
    private String propUnit;
    private CompareOpEnum op;
    private String oprand;
    private Integer delayTime;
    private Integer startDelayTime;
    private Integer endDelayTime;
}
