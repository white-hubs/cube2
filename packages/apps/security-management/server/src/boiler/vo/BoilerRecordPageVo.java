package com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BoilerRecordPageVo {
    private String deviceName;
    private String areaName;
    private BigDecimal waterValue;
    private BigDecimal temperature;
    private BigDecimal electricityValue;
    private LocalDateTime collectTime;

}
