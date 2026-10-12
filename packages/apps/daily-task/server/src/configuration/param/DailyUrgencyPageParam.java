package com.guhecloud.cube.biz.daily.task.configuration.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 紧急程度分页查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DailyUrgencyPageParam extends Paging {
    /**
     * 紧急程度名称（模糊查询）
     */
    private String name;

    /**
     * 任务类型（精确查询）
     */
    private String dailyType;
}
