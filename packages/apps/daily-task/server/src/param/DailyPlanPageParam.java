package com.guhecloud.cube.biz.daily.task.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DailyPlanPageParam extends Paging {

    /**
     * 日常任务类型
     */
    @NotBlank(message = "类型不能为空")
    private String dailyType;
    /**
     * 任务类型Id
     */
    private Long taskCategoryId;
    /**
     * 紧急程度id
     */
    private Long urgencyId;
    private String status;
    private String name;
    /**
     * 执行时间
     */
    private Period createTime;
    /**
     * 计划有效期
     */
    private Period startAndEndTime;
}
