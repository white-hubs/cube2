package com.guhecloud.cube.biz.daily.task.configuration.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 紧急程度插入参数
 */
@Data
public class DailyUrgencyInsertParam {
    /**
     * 紧急程度等级
     */
    @NotNull(message = "紧急程度等级不能为空")
    private Integer level;

    /**
     * 紧急程度名称
     */
    @NotBlank(message = "紧急程度名称不能为空")
    private String name;

    /**
     * 任务类型
     */
    @NotBlank(message = "任务类型不能为空")
    private String dailyType;
}
