package com.guhecloud.cube.base.flow.flowable.task.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.base.flow.flowable.task.domain.TaskOperateInfo;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.typehandler.JsonObjectArrayTypeHandler;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/10/28 14:03
 * @description 任务操作日志
 */
@Data
@Builder
@TableName(value = "task_operate_log", autoResultMap = true)
@AllArgsConstructor
@NoArgsConstructor
public class TaskOperateLogPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String businessKey;
    private String operateNodeName;
    @TableField(typeHandler = JsonObjectArrayTypeHandler.class, javaType = true)
    private List<TaskOperateInfo> taskInfos;

    @TableField(exist = false)
    private String taskInfosJson;
    private String taskDefinitionKey;
    private String processInstanceId;
    private TaskOperationType taskOperationType;
}
