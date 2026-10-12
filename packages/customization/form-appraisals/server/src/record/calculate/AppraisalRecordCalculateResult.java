package com.guhecloud.cube.srv.gen.appraisals.server.record.calculate;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 表单考核主记录计算结果。
 */
@Data
public class AppraisalRecordCalculateResult {
    /**
     * 原始总分。
     */
    private BigDecimal originTotalScore;
    /**
     * 原始抽查分。
     */
    private BigDecimal originSpotCheckScore;
    /**
     * 原始检查分。
     */
    private BigDecimal originInspectionScore;
    /**
     * 最终总分。
     */
    private BigDecimal finalTotalScore;
    /**
     * 最终抽查分。
     */
    private BigDecimal finalSpotCheckScore;
    /**
     * 最终检查分。
     */
    private BigDecimal finalInspectionScore;
    /**
     * 计算明细。
     */
    private List<AppraisalRecordDetailCalculateResult> details = new ArrayList<>();
}
