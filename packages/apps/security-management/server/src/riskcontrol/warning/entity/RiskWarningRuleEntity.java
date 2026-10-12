package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.warning.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_warning_rule")
public class RiskWarningRuleEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String warningType;
    private String conditionJson;
    private String severity;
    private String notifyChannels;
    private Integer disposeDeadlineDays;
    private Boolean enabled;
}
