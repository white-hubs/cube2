package com.guhecloud.cube.biz.daily.task.record.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecordPageResult {
    private Long id;
    /**
     * 日常任务类型
     */
    private String dailyType;
    private Long taskId;
    private String taskName;
    /**
     * 执行时间
     */
    private LocalDateTime executionTime;
    /**
     * 执行人员id
     */
    private Long executorId;
    private String executorName;
    /**
     * 关联对象名称
     */
    private String targetName;
    /**
     * 执行结果(正常异常)
     */
    private String executionResult;
    /**
     * 执行记录聚合状态
     */
    private String recordStatus;
    private String exceptionStatus;
    private Long repairUserId;
    private String repairUserName;
    private Boolean allowRepair;
}
