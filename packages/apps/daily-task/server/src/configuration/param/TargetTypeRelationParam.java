package com.guhecloud.cube.biz.daily.task.configuration.param;

import com.guhecloud.cube.biz.daily.task.enums.TaskTargetTypeEnum;
import lombok.Data;

/**
 * 关联对象参数
 */
@Data
public class TargetTypeRelationParam {
    /**
     * 对象类型
     */
    private TaskTargetTypeEnum type;

    /**
     * 目标类型ID
     */
    private String targetTypeId;
}