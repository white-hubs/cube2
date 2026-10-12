package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.guhecloud.cube.srv.gen.energymanagement.server.common.CarbonCal;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author appet
 * @Description 每日用量
 * @date 2021-12-30 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnergyDailyUsageForExportVO extends CarbonCal {

    //
    private Long id;
    private LocalDateTime statDate;
    private String statDateStr;
    //    private String instrumentType;
    private String instrumentTypeName;
    private BigDecimal dValue;
    private BigDecimal fValue;
    private BigDecimal pValue;
    private BigDecimal gValue;
    private BigDecimal jValue;
}