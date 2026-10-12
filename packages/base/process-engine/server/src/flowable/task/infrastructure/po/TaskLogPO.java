package com.guhecloud.cube.base.flow.flowable.task.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@TableName("task_log")
@NoArgsConstructor
public class TaskLogPO extends BasicEnhancedEntity<Long> {
    private Long id;
    private String taskKey;
    private String processInstanceId;
    private String operationInfo;

    public TaskLogPO(Long id, String taskKey, String processInstanceId, String operationInfo) {
        this.id = id;
        this.taskKey = taskKey;
        this.processInstanceId = processInstanceId;
        this.operationInfo = operationInfo;
    }
}
