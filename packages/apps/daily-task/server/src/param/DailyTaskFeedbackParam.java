package com.guhecloud.cube.biz.daily.task.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DailyTaskFeedbackParam {
    /**
     * 任务id
     */
    @NotNull(message = "任务id不能为空")
    private Long taskId;
    /**
     * 进度(0-100)
     */
    @NotNull(message = "进度不能为空")
    private BigDecimal schedule;
    /**
     * 描述
     */
    private String description;
    /**
     * 文件id，多个用逗号分隔
     */
    private String files;
}
