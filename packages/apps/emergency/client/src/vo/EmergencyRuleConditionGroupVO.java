package com.guhecloud.cube.biz.emergency.client.vo;

import lombok.Data;

import java.util.List;

@Data
public class EmergencyRuleConditionGroupVO {

    private Long id;

    private List<EmergencyRuleConditionCollectionVO> emergencyRuleConditions;

    private EmergencyRuleTimeVO emergencyRuleTime;
}
