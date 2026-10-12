package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_assessment_option")
public class RiskAssessmentOptionEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long dimensionId;
    private String optionName;
    private BigDecimal score;
    private Integer sortNo;
}
