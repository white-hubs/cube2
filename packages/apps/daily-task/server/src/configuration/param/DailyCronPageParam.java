package com.guhecloud.cube.biz.daily.task.configuration.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询任务周期分页参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DailyCronPageParam extends Paging {
    /**
     * 周期名称（模糊查询）
     */
    private String name;

    /**
     * 任务类型（精确查询）
     */
    private String dailyType;
}

