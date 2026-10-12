package com.guhecloud.cube.base.flow.flowable.task.application.validator;

import com.guhecloud.cube.base.flow.flowable.common.ProcessHelper;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
 * @author 剑秋
 * @description
 * @date 2022/9/16
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskHandleValidator {
    private final ProcessHelper helper;

    /**
     * 任务归属校验
     */
    public Task attributionVerify(String taskId, String account) {
        Task task = helper.checkTask(taskId);
        if (!account.equals(task.getAssignee())) {
            throw new ServiceException("当前用户不能处理该任务");
        }
        return task;
    }
}
