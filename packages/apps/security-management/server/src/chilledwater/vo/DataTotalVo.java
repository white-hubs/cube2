package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DataTotalVo {
    private BigDecimal duration;
    private BigDecimal flow;
    private BigDecimal total;
}
