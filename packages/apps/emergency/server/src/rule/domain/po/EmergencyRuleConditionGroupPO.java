package com.guhecloud.cube.biz.emergency.server.rule.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@TableName("emergency_rule_condition_group")
public class EmergencyRuleConditionGroupPO {

    /**
     * 条件组主键
     */
    private Long id;

    /**
     * 规则主键
     */
    private Long emergencyRuleId;
}
