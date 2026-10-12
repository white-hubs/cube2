package com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BoilerDataVo {
    private String deviceName;
    private String timeFlag;
    private BigDecimal dataValue;
}
