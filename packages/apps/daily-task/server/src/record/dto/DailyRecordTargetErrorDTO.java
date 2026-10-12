package com.guhecloud.cube.biz.daily.task.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 执行记录DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecordTargetErrorDTO {
    private Long id;
    private Long taskId;
    private String taskName;
    private Long recordId;
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
     * 对象id
     */
    private Long targetId;
    private String targetCode;
    private String targetCategory;
    private String targetName;
    /**
     * 对象的错误内容
     */
    private String targetErrorNames;
}
