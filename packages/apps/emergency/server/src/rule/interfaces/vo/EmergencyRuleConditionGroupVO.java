package com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyRuleConditionGroupVO {

    private Long id;

    /**
     * 预案规则条件列表
     */
    private List<EmergencyRuleConditionCollectionVO> emergencyRuleConditions;

    /**
     * 条件组时段配置
     */
    private EmergencyRuleTimeVO emergencyRuleTime;
}
