package com.guhecloud.cube.base.flow.flowable.task.application.operation;

import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractTaskOperation<T extends TaskOperationCommand> implements TaskOperation<T> {
    /**
     * 局部处理器
     */
    private List<LocalTaskOperationHandler<TaskOperationCommand>> localHandlers;
    /**
     * 全局处理器 通过动态代理去注入
     */
    private List<GlobalTaskOperationHandler> globalPassHandlers;

    public synchronized void exec(T passCommand) {
        // 运行过程中动态注入全局前置处理器  全局后置处理器
        Map<String, ?> beansOfType = ApplicationContextHolder.getBeansOfType(GlobalTaskOperationHandler.class);
        beansOfType.values().forEach(value -> {
            if (CollectionUtils.isEmpty(globalPassHandlers)) {
                this.globalPassHandlers = new LinkedList<>();
            }
            globalPassHandlers.add((GlobalTaskOperationHandler) value);
        });
        if (CollectionUtils.isNotEmpty(this.globalPassHandlers)) {
            globalPassHandlers.forEach(GlobalTaskOperationHandler::before);
        }

        //局部处理器
        if (CollectionUtils.isNotEmpty(this.localHandlers)) {
            localHandlers.forEach(localHandler -> localHandler.before(passCommand));
        }

        execute(passCommand);

        if (CollectionUtils.isNotEmpty(this.localHandlers)) {
            localHandlers.forEach(localHandler -> localHandler.after(passCommand));
        }
        if (CollectionUtils.isNotEmpty(this.globalPassHandlers)) {
            globalPassHandlers.forEach(GlobalTaskOperationHandler::after);
        }

        if (CollectionUtils.isNotEmpty(globalPassHandlers)) {
            globalPassHandlers.clear();
        }

        if (CollectionUtils.isNotEmpty(localHandlers)) {
            localHandlers.clear();
        }
    }

    public void execute(T passCommand) {

    }

    /**
     * 添加局部处理器
     *
     * @param localHandler
     */
    public synchronized AbstractTaskOperation addLocalHandler(LocalTaskOperationHandler localHandler) {
        if (CollectionUtils.isEmpty(localHandlers)) {
            localHandlers = new LinkedList<>();
        }
        localHandlers.add(localHandler);
        return this;
    }

}
