package com.guhecloud.cube.biz.daily.task.record.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 日常记录分页查询对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DailyRecordPageParam extends Paging {
    private Long id;

    /**
     * 日常任务类型
     */
    @NotBlank(message = "类型不能为空")
    private String dailyType;
    private String taskName;

    /**
     * 任务id
     **/
    private List<Long> taskIds;
    /**
     * 执行时间
     */
    private Period executionTime;
    /**
     * 执行人员id
     */
    private String executorName;
    /**
     * 执行结果(DailyExecutionResult 正常异常)
     */
    private String executionResult;
    /**
     * 执行记录状态
     */
    private String recordStatus;
    /**
     * 执行记录状态，多选筛选
     */
    private List<String> recordStatuses;
}
