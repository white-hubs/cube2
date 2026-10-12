package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.area.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_area")
public class RiskAreaEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String areaCode;
    private String areaName;
    private String departmentId;
    private String rangeDesc;
    private String description;
    private String ownerUserId;
    private String areaType;
    private String gisGeometry;
    private String inherentRiskLevel;
    private String controlRiskLevel;
    private String riskLevel;
}
