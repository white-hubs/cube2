package com.guhecloud.cube.biz.daily.task.configuration.param;

import com.guhecloud.cube.biz.daily.task.enums.TaskTargetTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 修改执行项的结果关联参数
 */
@Data
public class DailyItemResultRelationUpdateParam {
    /**
     * 执行项ID
     */
    @NotNull(message = "执行项ID不能为空")
    private Long itemId;

    /**
     * 执行项名称
     */
    private String itemName;

    /**
     * 执行项内容
     */
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
    private String dailyType;
}

