package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransformerMeter extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //设备名称
    private Long deviceId;
    //统计方式
    private String statisticalMethod;
    //电表读数a
    private BigDecimal meterA;
    //电表读数b
    private BigDecimal meterB;
    //电量
    private BigDecimal currentQuantity;
    //功率因素
    private BigDecimal powerFactor;
    //a相电流
    private BigDecimal electricA;
    //a相温度
    private BigDecimal temperatureA;
    //b相电流
    private BigDecimal electricB;
    //b相温度
    private BigDecimal temperatureB;
    //c相电流
    private BigDecimal electricC;
    //c相温度
    private BigDecimal temperatureC;
    //配电设备
    private Long distributionDevice;
    //采集时间
    private LocalDateTime collectTime;

}
