package com.guhecloud.cube.biz.daily.task.configuration.param;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 修改执行标准参数
 */
@Data
public class DailyStandardUpdateParam {
    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

    /**
     * 标准名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 任务种类ID（多个ID用逗号分隔，如：1,2,3）
     */
    private String categoryIds;

    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 关联对象列表
     */
    @Valid
    private List<TargetTypeRelationParam> targetTypeRelations;

    /**
     * 新增标准项列表
     */
    @Valid
    private List<ItemParam> items;

    /**
     * 任务类型
     */
    private String dailyType;

}

