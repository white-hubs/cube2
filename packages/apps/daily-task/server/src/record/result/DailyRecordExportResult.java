package com.guhecloud.cube.biz.daily.task.record.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 执行记录导出对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecordExportResult {
    private Long id;
    private String taskName;
    private Long taskId;
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
     * 执行结果(正常异常)
     */
    private String executionResult;
    /**
     * 记录的错误内容
     */
    private String recordErrorNames;
    /**
     * 记录的对象
     */
    private String targetNames;
    /**
     * 对象的错误内容
     */
    private String targetErrorNames;
}
