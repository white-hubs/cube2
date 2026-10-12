package com.guhecloud.cube.base.flow.flowable.task.application.event.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pengjian
 * @date 2022/9/5 14:20
 * @description
 */
@Slf4j
@Component("taskEventListener")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskInstanceEventListener {

    private final List<TaskInstanceEventHandler> taskInstanceEventHandlers;

    private static final String ASSIGNMENT = "assignment";
    private static final String CREATE = "create";
    private static final String COMPLETE = "complete";

    public void execute(DelegateTask delegateTask) {
        Object completedInstances = delegateTask.getExecution().getParent().getVariable("nrOfCompletedInstances");
        Object nrOfActiveInstances = delegateTask.getExecution().getParent().getVariable("nrOfActiveInstances");
        Map<String, Object> variables = new HashMap<>();
        variables.put("nrOfCompletedInstances", completedInstances);
        variables.put("nrOfActiveInstances", nrOfActiveInstances);
        delegateTask.getExecution().setVariables(variables);
        String eventName = delegateTask.getEventName();
        if (CollectionUtils.isNotEmpty(taskInstanceEventHandlers)) {
            if (eventName.equals(ASSIGNMENT)) {
                taskInstanceEventHandlers.forEach(taskInstanceEventHandler -> taskInstanceEventHandler.assignment(delegateTask));
            }

            if (eventName.equals(CREATE)) {
                taskInstanceEventHandlers.forEach(taskInstanceEventHandler -> {
                    long currentTimeMillis = System.currentTimeMillis();
                    taskInstanceEventHandler.create(delegateTask);
                    String aClass = taskInstanceEventHandler.getClass().getName();
                    log.info("taskCreat {} cost time:{}", aClass, System.currentTimeMillis() - currentTimeMillis);
                });
            }

            if (eventName.equals(COMPLETE)) {
                taskInstanceEventHandlers.forEach(taskInstanceEventHandler -> taskInstanceEventHandler.complete(delegateTask));
            }

        }
    }

}
