package com.guhecloud.cube.biz.subsystem.broadcast.server.statistics.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatisticsPlayRecordVo {
    private String name;
    private Integer num;
    private BigDecimal ratio = BigDecimal.ZERO;
}
