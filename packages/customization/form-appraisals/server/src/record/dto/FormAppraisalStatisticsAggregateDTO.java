package com.guhecloud.cube.srv.gen.appraisals.server.record.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 周期聚合结果
 */
@Data
public class FormAppraisalStatisticsAggregateDTO {
    private Integer assessmentPeriod;
    private BigDecimal originTotalScore;
    private BigDecimal finalTotalScore;
    private BigDecimal deductScore;
    private LocalDateTime assessmentBeginTime;
    private LocalDateTime assessmentEndTime;
    private LocalDateTime previewGeneratedAt;
}
