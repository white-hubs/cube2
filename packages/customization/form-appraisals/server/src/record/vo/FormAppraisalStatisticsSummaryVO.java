package com.guhecloud.cube.srv.gen.appraisals.server.record.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 考核统计汇总
 */
@Data
public class FormAppraisalStatisticsSummaryVO {
    /**
     * 周期总满分
     */
    private BigDecimal originTotalScore;
    /**
     * 总实得分
     */
    private BigDecimal finalTotalScore;
    /**
     * 总扣分
     */
    private BigDecimal deductScore;
}
