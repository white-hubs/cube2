package com.guhecloud.cube.srv.gen.appraisals.server.record.calculate;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 考核数据采集结果
 * 封装采集后的数值和原始值
 */
@Data
@AllArgsConstructor
public class AppraisalCollectResult {
    /**
     * 数值化的采集结果（用于计算）
     */
    private BigDecimal numericValue;
    /**
     * 原始采集值（用于展示）
     */
    private String rawValue;
}
