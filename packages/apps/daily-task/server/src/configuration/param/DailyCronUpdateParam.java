package com.guhecloud.cube.biz.daily.task.configuration.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 修改任务周期参数
 */
@Data
public class DailyCronUpdateParam {
    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

    /**
     * 周期名称
     */
    private String name;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * 描述
     */
    private String description;

    /**
     * 任务类型
     */
    private String dailyType;
}

