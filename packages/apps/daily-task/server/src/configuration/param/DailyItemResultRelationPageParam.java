package com.guhecloud.cube.biz.daily.task.configuration.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 查询执行项的结果关联分页参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DailyItemResultRelationPageParam extends Paging {
    /**
     * 执行项名称（模糊查询）
     */
    private String itemName;

    /**
     * 适配对象类型列表（支持多个类型查询，只要包含任一类型即匹配）
     */
    private List<String> targetTypes;

    /**
     * 任务类型（精确查询）
     */
    private String dailyType;
}

