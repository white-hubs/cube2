package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_measure_attachment")
public class RiskMeasureAttachmentEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long executionRecordId;
    private String fileId;
}
