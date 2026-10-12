package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WaterDataMonth extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String statisticalFlag;
    private String statisticalCycle;
    private String shiftMaster;
    private String shiftMasterPhone;
    private String gearPositionCode;
    private String departmentId;
    private String gearPositionAreaId;
    private String deviceCode;
    private BigDecimal unitTraffic;
    private BigDecimal duration;
    private BigDecimal flow;
    private BigDecimal unitPrice;
    private BigDecimal total;
    private String code;
    private String dataTime;
    private String department;
    private String gearPositionArea;
}
