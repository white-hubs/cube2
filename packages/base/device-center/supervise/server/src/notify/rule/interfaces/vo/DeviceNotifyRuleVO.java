package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.vo;

import lombok.Data;

import java.util.List;

@Data
public class DeviceNotifyRuleVO {
    private Long id;
    private Long deviceId;
    private String name;
    private String productCategoryName;
    private String prodName;
    private Boolean enabled;
    private List<DeviceNotifyRuleConditionVO> ruleConditions;
    private List<DeviceNotifyConfigVO> messageNotifies;
}
