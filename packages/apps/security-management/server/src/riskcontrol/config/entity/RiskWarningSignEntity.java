package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_warning_sign")
public class RiskWarningSignEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String signCode;
    private String signName;
    private String signType;
    private String iconFileId;
    private Boolean enabled;
}
