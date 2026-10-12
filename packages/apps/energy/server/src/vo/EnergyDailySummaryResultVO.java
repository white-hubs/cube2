package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EnergyDailySummaryResultVO {
    private Long id;
    private String dateStr;
    private LocalDateTime statDate;
    private BigDecimal ddValue;
    //碳排
    private BigDecimal dcValue;
    //综合能耗
    private BigDecimal zhValue;
    private BigDecimal fdValue;
    private BigDecimal pdValue;
    private BigDecimal gdValue;
    private BigDecimal jdValue;
    private LocalDateTime createTime;
}
