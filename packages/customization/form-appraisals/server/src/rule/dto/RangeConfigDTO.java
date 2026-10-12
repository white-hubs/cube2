
package com.guhecloud.cube.srv.gen.appraisals.server.rule.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 扣分区间配置 DTO
 * 定义不同数值范围对应的扣分值
 */
@Data
public class RangeConfigDTO {
    /**
     * 区间起始值
     */
    private BigDecimal startValue;
    /**
     * 区间结束值
     */
    private BigDecimal endValue;
    /**
     * 该区间对应的扣分值
     */
    private BigDecimal deductScore;
}
