package com.guhecloud.cube.biz.emergency.server.time.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@TableName("emergency_rule_time_job")
@EqualsAndHashCode(callSuper = true)
public class EmergencyRuleTimeJobPO extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 规则id
     */
    private Long emergencyRuleId;

    /**
     * 条件组id
     */
    private Long emergencyRuleConditionGroupId;

    /**
     * 时间配置主键
     */
    private Long emergencyRuleTimeId;

    /**
     * 时间周期类型
     */
    private EmergencyConditionTimeTypeEnum timeType;

    /**
     * 时间条件
     */
    private String timeConditions;

    /**
     * 时间条件
     */
    private String ruleTimeConditions;

    /**
     * 操作类型
     */
    private String operateType;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 上次执行时间
     */
    private Long triggerLastTime;

    /**
     * 下次执行时间
     */
    private Long triggerNextTime;
}
