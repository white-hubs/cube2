package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 年用能
 */
@Data
public class YearEnergyParam {
    private Long id;
    /**
     * 年份
     */
    private String year;
    /**
     * 日期的年份
     */
    private LocalDateTime dateYear;
    /**
     * 能耗类型
     */
    private String instrumentType;
    /**
     * 能耗类型名称
     */
    private String instrumentName;
    /**
     * 用能值
     */
    private BigDecimal energyValue;
    /**
     * 数据来源
     */
    private String sourceType;
    /**
     * 燃料名称
     */
    private String fuelName;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 碳排因子
     */
    private BigDecimal carbonFactor;
    /**
     * 计算系数
     */
    private BigDecimal calculateProportion;
}
