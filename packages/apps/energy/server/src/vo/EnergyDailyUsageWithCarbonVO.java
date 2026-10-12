package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author appet
 * @Description 每日用量带碳排和综合能耗
 * @date 2021-12-30 15:12:36
 */
@Data
public class EnergyDailyUsageWithCarbonVO {
    //
    private Long id;
    private LocalDateTime statDate;
    private String instrumentType;
    private String instrumentTypeName;
    private BigDecimal dValue;
    //碳排
    private BigDecimal dcValue;
    //综合能耗
    private BigDecimal zhValue;

}