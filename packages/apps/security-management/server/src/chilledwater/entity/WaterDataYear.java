package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WaterDataYear extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String code;
    private String gearPositionCode;
    private String departmentId;
    private String gearPositionAreaId;
    private String deviceCode;
    private BigDecimal unitTraffic;
    private BigDecimal one;
    private BigDecimal two;
    private BigDecimal three;
    private BigDecimal four;
    private BigDecimal five;
    private BigDecimal six;
    private BigDecimal seven;
    private BigDecimal eight;
    private BigDecimal nine;
    private BigDecimal ten;
    private BigDecimal eleven;
    private BigDecimal twelve;
    private BigDecimal total;
    private String dataTime;
    private String department;
    private String gearPositionArea;
    private String shiftMaster;
    private BigDecimal duration;
    private BigDecimal flow;
}
