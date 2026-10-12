package com.guhecloud.cube.biz.daily.task.commons.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyTaskSummaryResult {
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 对象总数
     */
    private Long total;
    /**
     * 已检查对象数
     */
    private Long checked;
    /**
     * 异常对象数
     */
    private Long abnormal;
    /**
     * 完成进度
     */
    private BigDecimal progress;

    /**
     * 状态
     */
    private String status;
}
