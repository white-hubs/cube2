package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PowerDistributionBox extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //设备id
    private Long deviceId;
    //统计方式
    private String statisticalMethod;
    //电流a
    private BigDecimal meterA;
    //电流b
    private BigDecimal meterB;
    //配电箱当前总电流
    private BigDecimal boxElectricTotal;
    //电流c
    private BigDecimal meterC;
    //采集时间
    private LocalDateTime collectTime;
    //电量
    private BigDecimal currentQuantity;
    //高压侧电压a
    private BigDecimal voltageA;
    //高压侧电压B
    private BigDecimal voltageB;
    //高压侧电压C
    private BigDecimal voltageC;

}
