package com.guhecloud.cube.base.flow.flowable.definition.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.guhecloud.cube.base.flow.flowable.definition.domain.entity.UserTaskNodeConfig;
import com.guhecloud.cube.base.flow.flowable.extension.node.NodeType;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/8/26 10:40
 * @description 任务节点
 */
@Data
@TableName(value = "task_node", autoResultMap = true)
public class TaskNodePO {

    /**
     * 流程定义中的节点id
     */
    private String taskKey;

    /**
     * 节点类型
     */
    private NodeType nodeType;

    /**
     * 节点个性化配置
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private UserTaskNodeConfig config;

    /**
     * 流程定义key
     */
    private String processDefinitionKey;
}
