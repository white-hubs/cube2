package com.guhecloud.cube.biz.daily.task.record.result;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 结果详情
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecordDetail {
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
    private Long fileRefKey;
    private List<FileVO> fileList;
    private String remark;
    /**
     * 执行结果(正常异常)
     */
    private String executionResult;
    /**
     * 执行记录聚合状态
     */
    private String recordStatus;
    /**
     * 记录的项
     */
    private List<DailyRecordItemResult> recordItemList;
    /**
     * 记录的对象
     */
    private List<DailyRecordTargetResult> targetList;
    /**
     * 关联工单
     */
    private List<DailyRecordWorkOrderResult> workOrderList;

    private List<DailyRecordExceptionResult> exceptionList;
}
