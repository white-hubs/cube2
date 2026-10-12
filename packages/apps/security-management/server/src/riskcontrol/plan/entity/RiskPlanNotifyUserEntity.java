package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.plan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_plan_notify_user")
public class RiskPlanNotifyUserEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long planId;
    private String userId;
}
