package com.guhecloud.cube.biz.daily.task.configuration.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 修改执行标准状态参数
 */
@Data
public class DailyStandardUpdateStatusParam {
    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

    /**
     * 是否启用
     */
    @NotNull(message = "启用状态不能为空")
    private Boolean enable;
}

