package com.guhecloud.cube.biz.daily.task.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 计划启停入参对象
 */
@Data
public class DailyPlanEnableParam {
    @NotNull(message = "计划ID不能为空")
    private Long id;
    @NotNull(message = "启用状态不能为空")
    private Boolean enable;
}
