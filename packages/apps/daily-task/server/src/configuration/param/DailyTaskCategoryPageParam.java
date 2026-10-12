package com.guhecloud.cube.biz.daily.task.configuration.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务类别分页参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DailyTaskCategoryPageParam extends Paging {
    /**
     * 任务类型名称（模糊查询）
     */
    private String name;

    /**
     * 任务类型（精确查询）
     */
    private String dailyType;
}
