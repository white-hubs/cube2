package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@TableName("att_scheduling_rule_classes")
@EqualsAndHashCode(callSuper = true)
public class AttSchedulingRuleClassesPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * 班次id
     */
    private Long id;
    /**
     * 规则id
     */
    private Long ruleId;
    /**
     * 星期(枚举)
     */
    private String week;
}
