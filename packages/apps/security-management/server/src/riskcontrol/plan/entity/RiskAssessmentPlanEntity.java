package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.plan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_assessment_plan")
public class RiskAssessmentPlanEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long unitId;
    private String planName;
    private String cycleType;
    private LocalDateTime nextExecuteTime;
    private LocalDateTime lastExecuteTime;
    private String status;
}
