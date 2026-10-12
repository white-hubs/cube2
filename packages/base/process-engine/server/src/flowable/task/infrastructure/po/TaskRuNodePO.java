package com.guhecloud.cube.base.flow.flowable.task.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.guhecloud.cube.base.flow.flowable.definition.domain.entity.UserTaskNodeConfig;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/8/29 17:50
 * @description
 */
@Data
@TableName(value = "task_ru_node", autoResultMap = true)
public class TaskRuNodePO {
    private String taskKey;
    private String processInstanceId;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private UserTaskNodeConfig taskConfig;
    private String processDefinitionKey;
}
