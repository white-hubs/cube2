package com.guhecloud.cube.biz.daily.task.record.param;

import lombok.Data;

import java.util.List;

/**
 * 批量查询记录详情参数对象
 */
@Data
public class DailyRecordTargetBatchParam {
    /**
     * 记录ID
     */
    private List<Long> ids;
    /**
     * 执行人
     */
    private String executor;
    /**
     * 任务ID集合
     */
    private List<Long> taskIds;
    /**
     * 目标ID集合
     */
    private List<Long> targetIds;
    /**
     * 执行结果
     */
    private String executorResult;
}
