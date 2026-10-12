package com.guhecloud.cube.biz.daily.task.commons.result;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DailyTaskFeedbackResult {
    private Long id;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 进度
     */
    private BigDecimal schedule;
    /**
     * 描述
     */
    private String description;
    /**
     * 反馈人名称
     */
    private String username;
    /**
     * 反馈人部门id
     */
    private Long feedbackDepId;
    /**
     * 反馈人部门名称
     */
    private String feedbackDepName;
    /**
     * 反馈时间
     */
    private LocalDateTime feedbackTime;
    /**
     * 文件列表
     */
    private List<FileVO> fileList;
}
