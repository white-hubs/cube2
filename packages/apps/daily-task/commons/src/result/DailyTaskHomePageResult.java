package com.guhecloud.cube.biz.daily.task.commons.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/21 09:33
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyTaskHomePageResult {

    /**
     * 任务数
     */
    private Long dailyTaskNum;

    /**
     * 环比
     */
    private BigDecimal dailyTaskNumRate;

    /**
     * 进行中的任务数
     */
    private Long processTaskNum;

    /**
     * 进行中的任务数环比
     */
    private BigDecimal processTaskNumRate;

    /**
     * 未完成任务数
     */
    private Long uncompletedTaskNum;
    /**
     * 未完成任务数环比
     */
    private BigDecimal uncompletedTaskNumRate;

}
