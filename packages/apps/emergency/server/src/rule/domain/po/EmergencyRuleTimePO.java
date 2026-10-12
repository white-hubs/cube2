package com.guhecloud.cube.biz.emergency.server.rule.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 报警规则触发时段配置
 *
 * @author lanchun
 * @date 2023/6/6 9:38
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("emergency_rule_time")
public class EmergencyRuleTimePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 预案规则主键
     */
    private Long emergencyRuleId;

    /**
     * 条件组主键
     */
    private Long groupId;

    /**
     * 时段类型
     */
    private EmergencyConditionTimeTypeEnum timeType;

    /**
     * 时间条件
     */
    private String timeConditions;
}
