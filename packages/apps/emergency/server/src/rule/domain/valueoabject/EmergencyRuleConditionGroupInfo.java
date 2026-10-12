package com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject;

import lombok.Getter;

import java.util.List;

@Getter
public class EmergencyRuleConditionGroupInfo {

    /**
     * 条件组主键
     */
    private Long id;

    /**
     * 规则条件信息
     */
    private List<EmergencyRuleConditionInfo> emergencyRuleConditionInfos;

    /**
     * 规则触发时段配置
     */
    private EmergencyRuleTimeInfo emergencyRuleTimeInfo;

    public EmergencyRuleConditionGroupInfo(Long id, List<EmergencyRuleConditionInfo> emergencyRuleConditionInfos,
                                           EmergencyRuleTimeInfo emergencyRuleTimeInfo
                                          ) {
        this.id = id;
        this.emergencyRuleConditionInfos = emergencyRuleConditionInfos;
        this.emergencyRuleTimeInfo = emergencyRuleTimeInfo;
    }
}
