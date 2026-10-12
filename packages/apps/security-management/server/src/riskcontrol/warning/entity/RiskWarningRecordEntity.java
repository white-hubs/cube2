package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.warning.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_warning_record")
public class RiskWarningRecordEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long ruleId;
    private Long unitId;
    private Long objectId;
    private String severity;
    private String warningStatus;
    private LocalDateTime triggerTime;
    private LocalDateTime disposeTime;
    private String disposeDesc;
    private String disposeResult;
}
