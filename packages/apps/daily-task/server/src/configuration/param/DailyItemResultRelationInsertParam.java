package com.guhecloud.cube.biz.daily.task.configuration.param;

import com.guhecloud.cube.biz.daily.task.enums.TaskTargetTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 新增执行项的结果关联参数
 */
@Data
public class DailyItemResultRelationInsertParam {
    /**
     * 执行项名称
     */
    @NotNull(message = "执行项名称不能为空")
    private String itemName;

    /**
     * 执行项内容
     */
    @NotNull(message = "执行项内容不能为空")
    private String itemContent;

    /**
     * 操作指引
     */
    private String operationGuide;

    /**
     * 适配对象类型列表（支持多个类型）
     */
    private List<TaskTargetTypeEnum> targetTypes;

    /**
     * 执行结果ID
     */
    @NotNull(message = "执行结果ID不能为空")
    private Long resultId;

    /**
     * 默认结果项
     */
    private String defaultResultItem;

    /**
     * 必须拍照的结果项
     */
    private String mustTakePhotoResultItems;

    /**
     * 必须备注的结果项
     */
    private String mustRemarkResultItems;

    /**
     * 标签
     */
    private String tag;

    /**
     * 任务类型
     */
    @NotNull(message = "任务类型不能为空")
    private String dailyType;

    /**
     * 是否临时添加
     */
    private Boolean isTemp = false;

    /**
     * id
     */
    private Long id;
}

