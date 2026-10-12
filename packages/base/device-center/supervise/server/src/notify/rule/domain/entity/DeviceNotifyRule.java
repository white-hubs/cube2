package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.entity;

import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.valueobj.DeviceNotifyConfig;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.valueobj.DeviceNotifyRuleCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class DeviceNotifyRule {
    private Long id;
    private Long deviceId;
    private Long iotRuleId;
    private Boolean enabled;
    private List<DeviceNotifyRuleCondition> conditions;
    private List<DeviceNotifyConfig> notifyConfigs;
    private Integer tenant;
}
