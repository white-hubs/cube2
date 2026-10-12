package com.guhecloud.cube.base.flow.flowable.task.application.operation;

public interface LocalTaskOperationHandler<T extends TaskOperationCommand> {
    void before(T passCommand);

    void after(T passCommand);
}
