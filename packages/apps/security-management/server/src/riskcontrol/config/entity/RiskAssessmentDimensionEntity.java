package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_assessment_dimension")
public class RiskAssessmentDimensionEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long methodId;
    private String dimensionCode;
    private String dimensionName;
    private String scoreRange;
    private Integer sortNo;
}
