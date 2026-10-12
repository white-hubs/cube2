package com.guhecloud.cube.biz.daily.task.commons.result;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/23 14:47
 * @Description:
 */
@Data
public class DailyRecordResult {
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
     * 执行结果(正常异常)
     */
    private String executionResult;
    /**
     * 执行记录聚合状态
     */
    private String recordStatus;

    private String remark;

    /**
     * 关联文件
     */
    private List<FileVO> fileList;
}
