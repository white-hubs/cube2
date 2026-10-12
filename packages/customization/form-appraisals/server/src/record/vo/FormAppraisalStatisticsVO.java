package com.guhecloud.cube.srv.gen.appraisals.server.record.vo;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalPeriodTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 考核统计看板结果
 */
@Data
public class FormAppraisalStatisticsVO {
    private Integer assessmentYear;
    private AppraisalPeriodTypeEnum periodType;
    /**
     * 当前期预览数据生成时间。
     */
    private LocalDateTime currentPreviewGeneratedAt;
    private FormAppraisalStatisticsSummaryVO summary;
    private List<FormAppraisalStatisticsPeriodVO> records;
}
