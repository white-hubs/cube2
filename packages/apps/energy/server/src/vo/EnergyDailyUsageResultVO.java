package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EnergyDailyUsageResultVO {
    private Long id;
    private LocalDateTime statDate;
    // 字符串的统计日期(statDate)
    private String time;
    private String instrumentType;
    private String instrumentTypeName;
    // 用能
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
