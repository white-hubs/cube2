package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_assessment_record")
public class RiskAssessmentRecordEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long objectId;
    private Long methodId;
    private String assessmentType;
    private BigDecimal totalScore;
    private String riskLevel;
    private String methodSnapshotJson;
    private String scoreDetailJson;
    private Boolean submitted;
}
