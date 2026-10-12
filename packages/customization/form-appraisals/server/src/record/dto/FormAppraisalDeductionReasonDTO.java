package com.guhecloud.cube.srv.gen.appraisals.server.record.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 表单考核扣分原因快照 DTO
 */
@Data
public class FormAppraisalDeductionReasonDTO {
    /**
     * 扣分来源名称
     */
    private String sourceRefName;
    /**
     * 字段标识
     */
    private String fieldKey;
    /**
     * 字段展示名称
     */
    private String fieldName;
    /**
     * 字段实际采集值/指标值
     */
    private String fieldValue;
    /**
     * 命中区间开始值
     */
    private BigDecimal matchedRangeStartValue;
    /**
     * 命中区间结束值
     */
    private BigDecimal matchedRangeEndValue;
    /**
     * 本次系统原始扣分值
     */
    private BigDecimal deductScore;
}
