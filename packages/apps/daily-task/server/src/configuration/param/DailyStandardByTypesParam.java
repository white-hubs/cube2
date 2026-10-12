package com.guhecloud.cube.biz.daily.task.configuration.param;

import com.guhecloud.cube.biz.daily.task.enums.TaskTargetTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 根据类型IDS查询执行标准参数
 */
@Data
public class DailyStandardByTypesParam {
    private TaskTargetTypeEnum type;
    @NotEmpty(message = "类型不能为空")
    private List<String> typeIds;
    private String dailyType;
}
