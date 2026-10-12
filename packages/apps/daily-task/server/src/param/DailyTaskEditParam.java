package com.guhecloud.cube.biz.daily.task.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DailyTaskEditParam {
    @NotNull(message = "主键不能为空")
    private Long id;
    private String name;
}
