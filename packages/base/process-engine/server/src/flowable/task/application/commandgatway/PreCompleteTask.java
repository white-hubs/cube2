package com.guhecloud.cube.base.flow.flowable.task.application.commandgatway;

import instance.command.CompleteTaskCommand;
import org.activiti.engine.task.Task;

/**
 * 预完成任务
 */
public interface PreCompleteTask {

    Task precompletion(CompleteTaskCommand completeTaskCommand);

}
