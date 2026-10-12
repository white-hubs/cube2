package com.guhecloud.cube.biz.daily.task.configuration.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询执行结果项分页参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DailyResultPageParam extends Paging {
    /**
     * 名称（模糊查询）
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 任务类型（精确查询）
     */
    private String dailyType;
}

