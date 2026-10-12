package com.guhecloud.cube.biz.daily.task.configuration.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 紧急程度更新参数
 */
@Data
public class DailyUrgencyUpdateParam {
    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

    /**
     * 紧急程度等级
     */
    private Integer level;

    /**
     * 紧急程度名称
     */
    private String name;

    /**
     * 任务类型
     */
    private String dailyType;
}
