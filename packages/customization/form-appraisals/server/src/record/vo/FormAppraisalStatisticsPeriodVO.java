package com.guhecloud.cube.srv.gen.appraisals.server.record.vo;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalPeriodTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考核统计周期行
 */
@Data
public class FormAppraisalStatisticsPeriodVO {
    /**
     * 考核年份
     */
    private Integer assessmentYear;
    /**
     * 周期类型（月度/季度/半年度）
     */
    private AppraisalPeriodTypeEnum periodType;
    /**
     * 周期序号（月度:1-12，季度:1-4，半年度:1-2）
     */
    private Integer assessmentPeriod;
    /**
     * 周期展示名称（如 2025年1月、2025年1季度）
     */
    private String periodLabel;
    /**
     * 周期开始时间
     */
    private LocalDateTime assessmentBeginTime;
    /**
     * 周期结束时间
     */
    private LocalDateTime assessmentEndTime;
    /**
     * 周期满分（汇总）
     */
    private BigDecimal originTotalScore;
    /**
     * 周期实得分（汇总）
     */
    private BigDecimal finalTotalScore;
    /**
     * 周期扣分（汇总）
     */
    private BigDecimal deductScore;
}
