package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.vo;

import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration.DeviceRuleConditionTypeEnum;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration.LogicalOpEnum;
import lombok.Data;

import java.util.List;

@Data
public class DeviceNotifyRuleConditionVO {
    private Long id;
    private DeviceRuleConditionTypeEnum conditionType;
    private String deviceEventId;
    private String deviceEventCode;
    private String deviceEventName;
    private String deviceEventType;
    private LogicalOpEnum logicalOp;
    private List<DeviceNotifyRuleConditionExpVO> ruleConditionExps;
}
