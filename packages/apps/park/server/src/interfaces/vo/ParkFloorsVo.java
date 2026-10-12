package com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ParkFloorsVo {
    private String areaName;
    private Long areaId;
    private BigDecimal rentedArea;
    //已租
    private BigDecimal rentedAreaRate = BigDecimal.ZERO;
    //未租
    private BigDecimal unleasedAreaRate = BigDecimal.ZERO;
    private BigDecimal total;
}
