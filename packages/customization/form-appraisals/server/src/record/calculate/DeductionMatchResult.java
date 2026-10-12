package com.guhecloud.cube.srv.gen.appraisals.server.record.calculate;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 扣分匹配结果
 * 封装匹配到的扣分值和区间描述
 */
@Data
@AllArgsConstructor
public class DeductionMatchResult {
    /**
     * 匹配到的扣分值
     */
    private BigDecimal deductScore;
    /**
     * 匹配的区间文本描述
     */
    private String matchedRangeText;
}
