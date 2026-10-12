package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.valueobj;

import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration.DeviceRuleConditionTypeEnum;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration.LogicalOpEnum;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DeviceNotifyRuleCondition {
    private DeviceRuleConditionTypeEnum conditionType;
    private String deviceEventId;
    private String deviceEventCode;
    private String deviceEventName;
    private String deviceEventType;
    private LogicalOpEnum logicalOp;
    private List<DeviceNotifyRuleConditionExp> conditionExps;
}
