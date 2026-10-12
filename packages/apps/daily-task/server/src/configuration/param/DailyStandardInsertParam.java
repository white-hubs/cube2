package com.guhecloud.cube.biz.daily.task.configuration.param;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 新增执行标准参数
 */
@Data
public class DailyStandardInsertParam {
    /**
     * 标准名称
     */
    @NotBlank(message = "标准名称不能为空")
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
    private List<TargetTypeRelationParam> targetTypeRelations;

    /**
     * 新增标准项列表
     */
    @Valid
    private List<ItemParam> items;

    /**
     * 任务类型
     */
    @NotBlank(message = "任务类型不能为空")
    private String dailyType;

}

