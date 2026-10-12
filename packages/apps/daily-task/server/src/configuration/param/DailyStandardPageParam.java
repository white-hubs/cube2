package com.guhecloud.cube.biz.daily.task.configuration.param;

import com.guhecloud.cube.biz.daily.task.enums.TaskTargetTypeEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询执行标准分页参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DailyStandardPageParam extends Paging {
    /**
     * 标准名称（模糊查询）
     */
    private String name;

    /**
     * 任务种类ID
     */
    private String categoryId;

    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 关联对象类型
     */
    private TaskTargetTypeEnum targetType;

    /**
     * 关联对象类型ID
     */
    private Long targetTypeId;

    /**
     * 任务类型（精确查询）
     */
    private String dailyType;
}

