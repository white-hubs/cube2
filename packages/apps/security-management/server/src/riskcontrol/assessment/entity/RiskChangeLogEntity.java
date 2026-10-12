package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_change_log")
public class RiskChangeLogEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String businessType;
    private Long businessId;
    private String changeType;
    private String beforeJson;
    private String afterJson;
}
