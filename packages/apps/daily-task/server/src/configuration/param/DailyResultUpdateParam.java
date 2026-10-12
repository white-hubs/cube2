package com.guhecloud.cube.biz.daily.task.configuration.param;

import com.guhecloud.cube.biz.daily.task.commons.dto.DailyResultLabelDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 修改执行结果项参数
 */
@Data
public class DailyResultUpdateParam {
    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

    /**
     * 名称
     */
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
    private String dailyType;
}

