package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_measure_execution_record")
public class RiskMeasureExecutionRecordEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long measureId;
    private String executionResult;
    private String executionDesc;
    private String executorUserId;
    private LocalDateTime executionTime;
    private String gpsLocation;
}
