package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_object")
public class RiskObjectEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String objectCode;
    private String objectName;
    private Long unitId;
    /**
     * 资产管理服务资产 ID；riskcontrol 不维护本地资产台账。
     */
    private Long assetId;
    private String identifyRoundNo;
    private String responsibleUserId;
    private String status;
    private String inherentRiskLevel;
    private String controlRiskLevel;
    private String hazardDesc;
    private String accidentTypes;
    private String accidentCause;
    private String triggerFactor;
    private String emergencyMeasure;
}
