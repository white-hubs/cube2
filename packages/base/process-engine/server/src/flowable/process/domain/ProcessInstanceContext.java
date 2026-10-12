package com.guhecloud.cube.base.flow.flowable.process.domain;

import base.constant.VariableConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.activiti.engine.task.TaskInfo;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author pengjian
 * @date 2022/10/14 15:18
 * @description
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Component
public class ProcessInstanceContext {
    private ExtHistoricProcessInstance extProcessInstance;
    private String result;

    public ProcessInstanceContext(ExtHistoricProcessInstance extProcessInstance, String result) {
        this.extProcessInstance = extProcessInstance;
        this.result = result;
    }

    private ThreadLocal<TaskInfo> lastFinishedTaskThreadLocal = new ThreadLocal<>();

    public String getFormInstanceId() {
        Map<String, Object> processVariables = extProcessInstance.getProcessInstance().getProcessVariables();
        Object formInstanceId = processVariables.get(VariableConstant.FORM_INSTANCE_Id);
        return (String) formInstanceId;
    }

    public String getResult() {
        return extProcessInstance.getResult();
    }

    public TaskInfo getLastFinishedTask() {
        TaskInfo taskInfo = lastFinishedTaskThreadLocal.get();
        lastFinishedTaskThreadLocal.remove();
        return taskInfo;
    }

    public void setLastFinishedTask(TaskInfo taskInfo) {
        lastFinishedTaskThreadLocal.set(taskInfo);
    }
}
