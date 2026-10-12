package com.guhecloud.cube.base.flow.flowable.task.domain;

import com.guhecloud.cube.base.flow.flowable.definition.domain.entity.UserTaskNodeConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.activiti.engine.task.TaskInfo;

/**
 * @author pengjian
 * @date 2022/9/2 16:10
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskInstance {
    private String taskKey;
    private String processInstanceId;
    private UserTaskNodeConfig taskConfig;

    private TaskInfo taskInfo;

    /**
     * 任务处理结果
     */
    private String result;

    /**
     * 任务处理数据
     */
    private String data;

    private boolean isActived;

    public String getData() {

        return data;
    }
}
