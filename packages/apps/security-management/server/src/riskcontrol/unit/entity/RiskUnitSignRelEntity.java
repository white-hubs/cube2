package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_unit_sign_rel")
public class RiskUnitSignRelEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long unitId;
    private Long signId;
}
