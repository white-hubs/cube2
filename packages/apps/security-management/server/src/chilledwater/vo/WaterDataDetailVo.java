package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WaterDataDetailVo {
    private String dataTime;
    private BigDecimal total;
    private BigDecimal duration;
    private BigDecimal flow;
    private String deviceCode;
}
