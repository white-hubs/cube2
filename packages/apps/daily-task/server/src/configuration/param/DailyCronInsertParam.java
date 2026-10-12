package com.guhecloud.cube.biz.daily.task.configuration.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 新增任务周期参数
 */
@Data
public class DailyCronInsertParam {
    /**
     * 周期名称
     */
    @NotBlank(message = "周期名称不能为空")
    private String name;

    /**
     * cron表达式
     */
    @NotBlank(message = "cron表达式不能为空")
    private String cron;

    /**
     * 描述
     */
    private String description;

    /**
     * 任务类型
     */
    @NotBlank(message = "任务类型不能为空")
    private String dailyType;
}

