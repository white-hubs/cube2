package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_assessment_method")
public class RiskAssessmentMethodEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String methodCode;
    private String methodName;
    private String methodDesc;
    private String formula;
    private Boolean presetFlag;
    private Boolean enabled;
}
