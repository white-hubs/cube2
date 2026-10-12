package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_unit_type")
public class RiskUnitTypeEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String typeCode;
    private String typeName;
    private Integer defaultIdentifyCycleDays;
    private String identifyDimensionDesc;
}
