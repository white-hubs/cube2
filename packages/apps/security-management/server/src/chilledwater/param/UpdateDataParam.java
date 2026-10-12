package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateDataParam {
    private String date;
    private String deviceCode;
    private BigDecimal minute;
    private BigDecimal oldMinute;
}
