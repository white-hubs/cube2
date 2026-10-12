package com.guhecloud.cube.biz.daily.task.configuration.param;

import com.guhecloud.cube.biz.daily.task.commons.dto.DailyResultLabelDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 新增执行结果项参数
 */
@Data
public class DailyResultInsertParam {
    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;
    /**
     * 是否启用
     */
    private Boolean enable;
    /**
     * 结果项 List<DailyResultLabelDTO>
     */
    private List<DailyResultLabelDTO> resultLabelList;

    /**
     * 任务类型
     */
    @NotBlank(message = "任务类型不能为空")
    private String dailyType;
}

