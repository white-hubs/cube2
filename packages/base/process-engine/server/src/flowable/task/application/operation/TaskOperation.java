package com.guhecloud.cube.base.flow.flowable.task.application.operation;

public interface TaskOperation<T extends TaskOperationCommand> {
    void exec(T taskOperationCommand);
}
