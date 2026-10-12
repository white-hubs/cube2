package com.guhecloud.cube.biz.daily.task.configuration.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 任务类型插入参数
 */
@Data
public class DailyTaskCategoryInsertParam {
    /**
     * 任务类型名称
     */
    @NotBlank(message = "任务类型名称不能为空")
    private String name;

    /**
     * 任务类型
     */
    @NotBlank(message = "任务类型不能为空")
    private String dailyType;
}
