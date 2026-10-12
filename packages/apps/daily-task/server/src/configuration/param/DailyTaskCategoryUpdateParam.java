package com.guhecloud.cube.biz.daily.task.configuration.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 任务类别更新参数
 */
@Data
public class DailyTaskCategoryUpdateParam {
    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

    /**
     * 任务类型名称
     */
    private String name;

    /**
     * 任务类型
     */
    private String dailyType;
}
